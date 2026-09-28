package org.example;
import java.util.Scanner;
import java.util.List;

public class Main
{
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String BOLD = "\u001B[1m";

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        Cart cart = new Cart();
        OrderHistory history = new OrderHistory();

        Category electronics = new Category(1, "Електроніка");
        Category smartphones = new Category(2, "Смартфони");
        Category accessories = new Category(3, "Аксесуари");

        Product product1 = new Product(1,"Ноутбук", 19999.99,"Високопродуктивний ноутбук для роботи та ігор", electronics);
        Product product2 = new Product(2,"Смартфон", 12999.50,"Смартфон з великим екраном та високою автономністю", smartphones);
        Product product3 = new Product(3,"Навушники", 2499.00,"Бездротові навушники з шумозаглушенням", accessories);

        while (true) {
            System.out.println(BOLD + CYAN + "╔══════════════════════════════════════╗" + RESET);
            System.out.println(BOLD + CYAN + "║       🛒 ONLINE SHOP 🛒             ║" + RESET);
            System.out.println(BOLD + CYAN + "╚══════════════════════════════════════╝" + RESET);
            System.out.println(GREEN + "1 - Переглянути список товарів" + RESET);
            System.out.println(YELLOW + "2 - Додати товар для кошика" + RESET);
            System.out.println(MAGENTA + "3 - Переглянути кошик" + RESET);
            System.out.println(RED + "4 - Видалити товар з кошика" + RESET);
            System.out.println(GREEN + "5 - Зробити замовлення" + RESET);
            System.out.println(CYAN + "6 - Історія замовлень" + RESET);
            System.out.println(BOLD + "0 - Вихід" + RESET);
            System.out.print(BOLD + CYAN + "▶ Ваш вибір: " + RESET);

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println(BOLD + "📦 Список товарів:" + RESET);
                    System.out.println(product1);
                    System.out.println(product2);
                    System.out.println(product3);
                    break;
                case 2:
                    System.out.println("Введіть ID товару для додавання до кошика:");
                    int id = scanner.nextInt();

                    if (id==1) cart.addProduct(product1);
                    else if (id==2) cart.addProduct(product2);
                    else if (id==3) cart.addProduct(product3);
                    else System.out.println(RED + "Товар з таким ID не знайдено :( " + RESET);
                    break;
                case 3:
                    if (cart.isEmpty()) {
                        System.out.println(RED + "Кошик порожній. Додайте товари!" + RESET);
                    } else {
                        System.out.println(BOLD + GREEN + cart + RESET);
                    }
                    break;
                case 4:
                    if (cart.isEmpty()) {
                        System.out.println(RED + "Кошик порожній. Немає чого видаляти!" + RESET);
                    } else {
                        System.out.println(BOLD + YELLOW + "📋 Ваш кошик:" + RESET);
                        List<Product> products = cart.getProducts();
                        for (int i = 0; i < products.size(); i++) {
                            System.out.println(YELLOW + (i + 1) + ". " + products.get(i).getName() +
                                    " - " + String.format("%.2f", products.get(i).getPrice()) + " ₴" + RESET);
                        }
                        System.out.print(GREEN + "Введіть номер товару для видалення (або 0 для скасування): " + RESET);
                        int removeIndex = scanner.nextInt();
                        if (removeIndex == 0) {
                            System.out.println(CYAN + "Скасовано. " + RESET);
                        } else if (removeIndex > 0 && removeIndex <= products.size()) {
                            Product removed = products.get(removeIndex - 1);
                            cart.removeProduct(removeIndex - 1);
                            System.out.println(RED + "✗ " + removed.getName() + " було видалено з кошика!" + RESET);
                        } else {
                            System.out.println(RED + "Невірний номер!" + RESET);
                        }
                    }
                    break;
                case 5:
                    if (cart.isEmpty()) {
                        System.out.println(RED + "Кошик порожній. Додайте товари перед оформленням замовлення." + RESET);
                    }
                    else {
                        Order order = new Order(cart);
                        history.addOrder(order);
                        System.out.println(GREEN + "✅ Замовлення оформлено:" + RESET);
                        System.out.println(order);
                        cart.clear();
                    }
                    break;
                case 6:
                    history.displayHistory();
                    if (!history.isEmpty()) {
                        System.out.print(BOLD + CYAN + "Введіть номер замовлення для деталей (0 - назад): " + RESET);
                        int orderChoice = scanner.nextInt();
                        if (orderChoice > 0 && orderChoice <= history.getSize()) {
                            history.displayOrderDetails(orderChoice);
                        }
                    }
                    break;
                case 0:
                    System.out.println(GREEN + "Дякуємо, що використовували наш магазин!" + RESET);
                    return;
                default:
                    System.out.println(RED + "Невідома опція. Спробуйте ще раз." + RESET);
                    break;
            }
        }
    }
}
