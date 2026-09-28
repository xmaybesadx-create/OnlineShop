package org.example;
import lombok.Data;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Locale;

@Data
public class OrderHistory
{
    private List<Order> orders;
    private final String FILE_PATH = "orders_history.txt";
    private SimpleDateFormat dateFormat;

    public OrderHistory()
    {
        this.orders = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        loadFromFile();
    }

    public void addOrder(Order order)
    {
        orders.add(order);
        saveToFile();
    }

    public List<Order> getOrders()
    {
        return new ArrayList<>(orders);
    }

    public boolean isEmpty()
    {
        return orders.isEmpty();
    }

    public int getSize()
    {
        return orders.size();
    }

    private void saveToFile()
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH)))
        {
            writer.println("HISTORY_VERSION:1");
            writer.println("ORDERS:" + orders.size());
            for (int i = 0; i < orders.size(); i++)
            {
                Order order = orders.get(i);
                writer.println("ORDER:" + (i + 1));
                writer.println("DATE:" + dateFormat.format(new Date()));
                writer.println("STATUS:" + order.getStatus());
                writer.println("TOTAL:" + String.format(Locale.US, "%.2f", order.getTotalPrice()));
                writer.println("PRODUCT_COUNT:" + order.getProducts().size());
                for (Product p : order.getProducts())
                {
                    writer.println("PRODUCT:" + p.getId() + "|" + p.getName() + "|" +
                            String.format(Locale.US, "%.2f", p.getPrice()) + "|" + p.getDescription() + "|" + p.getCategory().getName());
                }
                writer.println("END_ORDER");
            }
        }
        catch (IOException e)
        {
            System.err.println("Помилка збереження історії: " + e.getMessage());
        }
    }

    private void loadFromFile()
    {
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH)))
        {
            String line;
            Order currentOrder = null;
            List<Product> currentProducts = null;
            String currentStatus = "";
            double currentTotal = 0;

            while ((line = reader.readLine()) != null)
            {
                if (line.startsWith("HISTORY_VERSION:")) continue;
                else if (line.startsWith("ORDER:"))
                {
                    currentOrder = new Order(new Cart());
                    currentProducts = new ArrayList<>();
                    currentStatus = "Нове";
                }
                else if (line.startsWith("DATE:")) continue;
                else if (line.startsWith("STATUS:")) currentStatus = line.substring(7);
                else if (line.startsWith("TOTAL:")) currentTotal = Double.parseDouble(line.substring(6));
                else if (line.startsWith("PRODUCT:"))
                {
                    String[] parts = line.substring(8).split("\\|");
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    String desc = parts[3];
                    String categoryName = parts[4];
                    Category cat = new Category(0, categoryName);
                    Product p = new Product(id, name, price, desc, cat);
                    currentProducts.add(p);
                }
                else if (line.equals("END_ORDER") && currentOrder != null)
                {
                    for (Product p : currentProducts)
                    {
                        currentOrder.addProduct(p);
                    }
                    currentOrder.setTotalPrice(currentTotal);
                    currentOrder.setStatus(currentStatus);
                    orders.add(currentOrder);
                    currentOrder = null;
                    currentProducts = null;
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("Помилка завантаження історії: " + e.getMessage());
        }
    }

    public void displayHistory()
    {
        if (isEmpty())
        {
            System.out.println(RED + "Історія порожня. Ви ще не зробили жодного замовлення." + RESET);
            return;
        }

        System.out.println(BOLD + CYAN + "═══════════════════════════════════════════" + RESET);
        System.out.println(BOLD + CYAN + "📜  ІСТОРІЯ ЗАМОВЛЕНЬ (" + orders.size() + " шт.)" + RESET);
        System.out.println(BOLD + CYAN + "═══════════════════════════════════════════" + RESET);

        for (int i = orders.size() - 1; i >= 0; i--)
        {
            Order order = orders.get(i);
            System.out.println();
            System.out.println(YELLOW + "┌──────────────────────────────────────┐" + RESET);
            System.out.println(YELLOW + "│ " + BOLD + "Замовлення #" + (i + 1) + RESET + YELLOW + "                        │" + RESET);
            System.out.println(YELLOW + "│ " + RESET + "Товари: " + order.getProducts().size() + " шт." + RESET + YELLOW + "              │" + RESET);
            System.out.println(YELLOW + "│ " + RESET + "Загалом: " + String.format("%.2f", order.getTotalPrice()) + " ₴" + RESET + YELLOW + "         │" + RESET);
            System.out.println(YELLOW + "│ " + RESET + "Статус: " + order.getStatus() + RESET + YELLOW + "                 │" + RESET);
            System.out.println(YELLOW + "└──────────────────────────────────────┘" + RESET);
        }
        System.out.println();
        System.out.println(BOLD + GREEN + "Усього замовлень: " + orders.size() + RESET);
        System.out.println(BOLD + GREEN + "Загальна сума всіх замовлень: " + String.format("%.2f", getTotalSum()) + " ₴" + RESET);
    }

    public void displayOrderDetails(int index)
    {
        if (index < 1 || index > orders.size())
        {
            System.out.println(RED + "Невірний номер замовлення!" + RESET);
            return;
        }
        Order order = orders.get(index - 1);
        System.out.println(BOLD + GREEN + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
        System.out.println(BOLD + GREEN + "📄 Деталі замовлення #" + index + RESET);
        System.out.println(BOLD + GREEN + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
        System.out.println(order);
        System.out.println(BOLD + GREEN + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
    }

    private double getTotalSum()
    {
        double sum = 0;
        for (Order order : orders) sum += order.getTotalPrice();
        return sum;
    }

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";
}
