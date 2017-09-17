package com.example.enzo.sqlitedemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.enzo.sqlitedemo.DAO.Order;
import com.example.enzo.sqlitedemo.DB.DbHelper;
import com.example.enzo.sqlitedemo.DB.OrderDbHelper;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {
    private EditText edtxt_id, edtxt_name, edtxt_price, edtxt_country;
    private TextView         txt_message;
    private DbHelper         mDbHelper;
    private OrderDbHelper    mOrderDbHelper;
    private OrderListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtxt_id = (EditText) findViewById(R.id.edtxt_id);
        edtxt_name = (EditText) findViewById(R.id.edtxt_name);
        edtxt_price = (EditText) findViewById(R.id.edtxt_price);
        edtxt_country = (EditText) findViewById(R.id.edtxt_country);

        // clear 3 others fields after id changed
        edtxt_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtxt_name.getText().clear();
                edtxt_price.getText().clear();
                edtxt_country.getText().clear();
            }
        });

        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
        Button btn_find = (Button) findViewById(R.id.btn_find);
        btn_find.setOnClickListener(this);
        Button btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);

        txt_message = (TextView) findViewById(R.id.txt_message);

        RecyclerView list_order = (RecyclerView) findViewById(R.id.list_order);
        list_order.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OrderListAdapter();
        list_order.setAdapter(mAdapter);

        mDbHelper = new DbHelper(this);
        mOrderDbHelper = new OrderDbHelper(mDbHelper);
        refreshList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }

    private void refreshList() {
        mAdapter.setData(mOrderDbHelper.getAll());
    }

    private int getEditTextId() {
        if (edtxt_id.getText() != null)
            return Integer.valueOf(edtxt_id.getText().toString());
        else
            return -1;
    }

    private String getEditTextName() {
        return edtxt_name.getText().toString();
    }

    private int getEditTextPrice() {
        if (edtxt_price.getText() != null)
            return Integer.valueOf(edtxt_price.getText().toString());
        else
            return -1;
    }

    private String getEditTextCountry() {
        return edtxt_country.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Order order = new Order(getEditTextId(), getEditTextName(), getEditTextPrice(), getEditTextCountry());
                mOrderDbHelper.save(order);
                refreshList();
                txt_message.setText("Save order : " + order.getId());
                break;
            case R.id.btn_delete:
                mOrderDbHelper.delete(getEditTextId());
                refreshList();
                txt_message.setText("Delete order : " + getEditTextId());
                break;
            case R.id.btn_find:
                Order orderFound = mOrderDbHelper.get(getEditTextId());
                if (orderFound != null) {
                    edtxt_id.setText(String.valueOf(orderFound.getId()));
                    edtxt_name.setText(orderFound.getProductName());
                    edtxt_price.setText(String.valueOf(orderFound.getOrderPrice()));
                    edtxt_country.setText(orderFound.getCountry());

                    txt_message.setText("Order found : " + orderFound.getId() + ", " +
                            orderFound.getProductName() + ", " + orderFound.getOrderPrice() + ", " +
                            orderFound.getCountry());
                    refreshList();
                } else
                    txt_message.setText("Order " + getEditTextId() + " not found.");
                break;
            case R.id.btn_update:
                Order order1 = new Order(getEditTextId(), getEditTextName(), getEditTextPrice(), getEditTextCountry());
                mOrderDbHelper.update(order1);
                refreshList();
                txt_message.setText("Update order : " + order1.getId());
                break;
        }
    }


    /**
     * OrderViewHolder
     */
    private class OrderViewHolder extends ViewHolder {
        public TextView txt_id, txt_name, txt_price, txt_country;

        public OrderViewHolder(View itemView) {
            super(itemView);
            txt_id = (TextView) itemView.findViewById(R.id.txt_id);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_country = (TextView) itemView.findViewById(R.id.txt_country);
        }
    }


    /**
     * OrderListAdapter
     */
    private class OrderListAdapter extends Adapter<OrderViewHolder> {
        private ArrayList<Order> orderList;

        public void setData(ArrayList<Order> data) {
            orderList = data;
            notifyDataSetChanged();
        }

        @Override
        public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.activity_main_list_order_item, parent, false);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderViewHolder holder, int position) {
            Order order = orderList.get(position);
            holder.txt_id.setText(String.valueOf(order.getId()));
            holder.txt_name.setText(order.getProductName());
            holder.txt_price.setText(String.valueOf(order.getOrderPrice()));
            holder.txt_country.setText(order.getCountry());
        }

        @Override
        public int getItemCount() {
            return orderList == null ? 0 : orderList.size();
        }
    }

}
