package org.example;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category
{
    private int id;
    private String name;

    @Override
    public String toString()
    {
        return "Категорія{" + "id=" + id + ", назва=" + name + '}';
    }
}
