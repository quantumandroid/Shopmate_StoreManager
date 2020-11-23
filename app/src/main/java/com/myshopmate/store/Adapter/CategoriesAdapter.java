package com.myshopmate.store.Adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.myshopmate.store.Model.Category;
import com.myshopmate.store.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends BaseAdapter implements SpinnerAdapter {

    Context mContext;
    LayoutInflater inflater;

    private List<Category> list = null;
    //this is used for filter
    private final ArrayList<Category> arrayList;

    // create constructor
    public CategoriesAdapter(Context context, List<Category> list) {

        mContext = context;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Category>();
        this.arrayList.addAll(list);

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Category getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.layout_category_spinner, null);
            // Locate the TextViews in listview_item.xml
            holder.categoryName = view.findViewById(R.id.category_name);
            // todo add here

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        final Category item = list.get(position);

        holder.categoryName.setText(item.getTitle());
        // todo add here

        holder.categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* // TODO Auto-generated method stub
                mContext.startActivity(new Intent(mContext, delat.class).putExtra("ID1", item.get("ID1")));
                ((Activity) mContext).finish();*/
            }
        });


        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }

    // declare list item view's
    public class ViewHolder {
        TextView categoryName;
    }


}