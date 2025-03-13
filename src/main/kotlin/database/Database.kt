package database

import ui.models.Item
import java.sql.Connection


object Database {
    var connection: Connection? = null

    fun isItemInDatabaseById(id: Long): Boolean {
        check(connection != null) { "Walay connected database" }
        val query = connection?.prepareStatement("SELECT COUNT(*) FROM public.\"Item\" WHERE id = $id")

        query?.executeQuery().use { rs ->
            return rs?.next() == true && rs.getInt(1) > 0
        }
    }

    fun getItemById(id: Long): Item {
        check(connection != null) { "Walay connected database" }
        check(isItemInDatabaseById(id)) { "Walay item na naay barcode na $id sa database" }
        val query = connection?.prepareStatement("SELECT * FROM public.\"Item\" WHERE id = $id")

        query?.executeQuery().use { rs ->
            check(rs?.next() == true || rs != null) { "Walay na kit.an" }

            return Item(
                id = rs!!.getLong("id"),
                price = rs.getDouble("price"),
                name = rs.getString("name")
            )
        }
    }

    fun insertItem(item: Item) {
        check(connection != null) { "Walay connected database" }
        check(!isItemInDatabaseById(item.id)) { "Naa nay item barcode na ${item.id} sa database" }

        val query =
            connection?.prepareStatement("INSERT INTO public.\"Item\" (id, name, price) VALUES (${item.id}, \'${item.name}\', ${item.price})")
        query?.executeUpdate()
    }

    fun updateAnItem(item: Item): Boolean {
        check(connection != null) { "Walay connected database" }
        check(isItemInDatabaseById(item.id)) { "Walay item na naay barcode na ${item.id} sa database" }

        val query =
            connection?.prepareStatement("UPDATE public.\"Item\" SET name = \'${item.name}\', price = ${item.price} WHERE id = ${item.id}")
        query?.executeUpdate()
        return true
    }

    fun deleteItemById(id: Long): Boolean {
        check(connection != null) { "Walay connected database" }
        check(isItemInDatabaseById(id)) { "Walay item na naay barcode na $id sa database" }

        val query = connection?.prepareStatement("DELETE FROM public.\"Item\" WHERE id = $id")
        query?.executeUpdate()
        return true
    }
}