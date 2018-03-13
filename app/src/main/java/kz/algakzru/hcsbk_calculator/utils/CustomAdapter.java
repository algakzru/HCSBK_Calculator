package kz.algakzru.hcsbk_calculator.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import kz.algakzru.hcsbk_calculator.R;

/**
 * Created by 816856 on 3/13/2018.
 */

public class CustomAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<ListItem> listItems;

    public CustomAdapter(Context context, List<ListItem> listItems) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_graphic_pogashenia, null);
        }

        ListItem listItem = getListItem(position);

        ((TextView) convertView.findViewById(R.id.tv_key)).setText(listItem.getKey());
        ((TextView) convertView.findViewById(R.id.tv_value)).setText(String.format(Locale.US, "%,.2f", listItem.getValue()));

        return convertView;
    }

    ListItem getListItem(int position) {
        return (ListItem) getItem(position);
    }

}
