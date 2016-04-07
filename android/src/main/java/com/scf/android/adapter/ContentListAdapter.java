package com.scf.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.scf.android.R;
import com.scf.shared.dto.ArtifactDTO;
import com.scf.shared.dto.CollectionDTO;
import com.scf.shared.dto.CommonDTO;

import java.util.List;

/**
 * Created by Ator on 4/7/16.
 */
public class ContentListAdapter extends BaseAdapter {
    private CollectionDTO collection;
    private Context context;

    public ContentListAdapter(CollectionDTO collection, Context context) {
        this.context = context;
        this.collection = collection;
    }

    public void setCollection(CollectionDTO collection) {
        this.collection = collection;
    }

    public CollectionDTO getCollection() {
        return collection;
    }

    @Override
    public int getCount() {
        return collection.getCollectionList().size() + collection.getArtifactList().size();
    }

    @Override
    public Object getItem(int position) {
        return getListDTOItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.listRowItemText);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listRowItemImage);
        CommonDTO colOrArtDTO = getListDTOItem(position);
        if (colOrArtDTO instanceof CollectionDTO) {
            textView.setText(((CollectionDTO) getListDTOItem(position)).getName());
            imageView.setImageResource(R.drawable.folder);
        } else {
            textView.setText(((ArtifactDTO) getListDTOItem(position)).getName());
            imageView.setImageResource(R.drawable.file);
        }

        return rowView;
    }

    public CommonDTO getListDTOItem(int position) {
        if (position < collection.getCollectionList().size()) {
            return collection.getCollectionList().get(position);
        } else if (position < collection.getCollectionList().size() + collection.getArtifactList().size()) {
            return collection.getArtifactList().get(position - collection.getCollectionList().size());
        } else {
            System.err.println("Wrong position value sent to getListDTOItem method [" + position + "]");
            return null;
        }
    }
}
