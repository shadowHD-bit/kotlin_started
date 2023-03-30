package com.example.lab5_krivikov_8k93

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView

class ItemAdapter(context: Context, items: ArrayList<Item>):
    BaseAdapter() {
    var ctx: Context = context
    var objects: ArrayList<Item> = items
    var inflater: LayoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // Формирование разметки, содержащей строку данных
    override fun getView(position: Int, convertView: View?,
                         parent: ViewGroup?): View {
        // Если разметка ещё не существует, создаём её по шаблону
        var view = convertView
        if (view == null)
            view = inflater.inflate(R.layout.listview_layout_item,
                parent, false)
        // Получение объекта с информацией о продукте
        val s = objects[position]
        // Заполнение элементов данными из объекта
        var v = view?.findViewById(R.id.item_title) as TextView
        v.text = s.title
        v = view.findViewById(R.id.item_kind) as TextView
        v.text = s.kind
        return view
    }
    // Получение элемента данных в указанной строке
    override fun getItem(position: Int): Any {
        return objects[position]
    }
    // Получение идентификатора элемента в указанной строке
    // Часто вместо него возвращается позиция элемента
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    // Получение количества элементов в списке
    override fun getCount(): Int {
        return objects.size
    }
}

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var items = ArrayList<Item>()
    private lateinit var con: SQLiteDatabase;

    private fun getItems() {
        val cursor = con.query("items",
            arrayOf("id", "kind", "title", "price", "weight", "photo"),
            null, null, null, null,null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val s = Item()
            s.id = cursor.getInt(0)
            s.kind = cursor.getString(1)
            s.title = cursor.getString(2)
            s.price = cursor.getDouble(3)
            s.weight = cursor.getDouble(4)
            s.photo = cursor.getString(5)
            items.add(s)
            cursor.moveToNext()
        }
        cursor.close()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.listItems)
        listView.adapter = ItemAdapter(this, items)
        listView.setOnItemClickListener { adapterView: AdapterView<*>,
                                          view1: View, i: Int, l: Long ->
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("index", i)
            intent.putExtra("item", items[i])
            startActivityForResult(intent, 0)
        }


        // Настройка плавающей кнопки
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, ItemActivity::class.java)
            startActivityForResult(intent, 0)
        }

        val db = SQLiteHelper(this);
        con = db.readableDatabase
        getItems()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
        {
            val index: Int = data?.getIntExtra("index", -1) ?: -1
            val item: Item = data?.getParcelableExtra("item") ?:Item()

            val cv = ContentValues()
            cv.put("kind", item.kind)
            cv.put("title", item.title)
            cv.put("price", item.price)
            cv.put("weight", item.weight)
            cv.put("photo", item.photo)
            if (index != -1) {
                items[index] = item
                cv.put("id", item.id)
                con.update("items", cv, "id=?", arrayOf(item.id.toString()))
            }
            else {
                items.add(item)
                con.insert("items", null, cv)
            }
            if (index != -1)
                items[index] = item
            else
                items.add(item)
            val listView: ListView = findViewById(R.id.listItems)
            (listView.adapter as ItemAdapter).notifyDataSetChanged()
        }
    }
}