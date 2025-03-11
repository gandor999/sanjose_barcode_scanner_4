package database

import ui.Item
import java.sql.Connection


object Database {
    var connection: Connection? = null

    fun isItemInDatabaseById(id: Long): Boolean {
        val query = connection?.prepareStatement("SELECT COUNT(*) FROM public.\"Item\" WHERE id = $id")

        query?.executeQuery().use { rs ->
            return rs?.next() == true && rs.getInt(1) > 0
        }
    }

    fun getItemById(id: Long): Item? {
        val query = connection?.prepareStatement("SELECT id, name, price FROM public.\"Item\" WHERE id = $id")

        query?.executeQuery().use { rs ->
            if (rs?.next() == false || rs == null ) return null

            if (isItemInDatabaseById(id)) {
                return Item(
                    id = rs.getLong("id"),
                    price = rs.getDouble("price"),
                    name = rs.getString("name")
                )
            }

            return null
        }
    }

    fun insertItem(item: Item): Boolean {
        return try {
            if (!isItemInDatabaseById(item.id)) {
                val query = connection?.prepareStatement("INSERT INTO public.\"Item\" (id, name, price) VALUES (${item.id}, ${item.name}, ${item.price})")

                query?.executeUpdate()

                return true
            }

            false
        } catch (e: Exception) {
            false
        }
    }
}