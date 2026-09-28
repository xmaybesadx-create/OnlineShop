package org.example;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart
{
    private List<Product> products;

    public Cart()
    {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts()
    {
        return new ArrayList<>(products);
    }

    public void addProduct(Product product)
    {
        products.add(product);
    }

    public void removeProduct(Product product)
    {
        products.remove(product);
    }

    public void removeProduct(int index)
    {
        if (index >= 0 && index < products.size()) {
            products.remove(index);
        }
    }

    public boolean isEmpty()
    {
        return products.isEmpty();
    }

    public double getTotalPrice()
    {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    public void clear()
    {
        products.clear();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("Кошик містить:\n");
        for (Product product : products) {
            sb.append(product.toString()).append("\n");
        }
        sb.append("Загальна вартість: ").append(getTotalPrice());
        return sb.toString();
    }
}
