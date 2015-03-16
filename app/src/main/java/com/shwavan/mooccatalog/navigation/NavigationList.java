package com.shwavan.mooccatalog.navigation;

import android.util.SparseIntArray;

import com.shwavan.mooccatalog.adapter.NavigationItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class NavigationList {

    public static List<NavigationItemAdapter> getNavigationAdapter(List<String> listNameItem, List<Integer> listIcon,
                                                                        List<Integer> listItensHeader, SparseIntArray sparceItensCount,
                                                                        int colorSelected, boolean removeSelector) {

        List<NavigationItemAdapter> mList = new ArrayList<>();
        if (listNameItem == null || listNameItem.size() == 0) {
            throw new RuntimeException("List of null or empty names. Solution: mListNameItem = new ArrayList <> (); mListNameItem.add (position, R.string.name);");
        }

        int icon;
        int count;
        boolean isHeader;

        for (int i = 0; i < listNameItem.size(); i++) {

            String title = listNameItem.get(i);

            NavigationItemAdapter mItemAdapter;

            icon = (listIcon != null ? listIcon.get(i) : 0);
            isHeader = (listItensHeader != null && listItensHeader.contains(i));
            count = (sparceItensCount != null ? sparceItensCount.get(i, -1) : -1);

            if (isHeader && icon > 0) {
                throw new RuntimeException("The value of the icon for a subHeader item should be 0");
            }

            if (!isHeader) {
                if (title == null) {
                    throw new RuntimeException("Enter the item name position " + i);
                }

                if (title.trim().equals("")) {
                    throw new RuntimeException("Enter the item name position " + i);
                }
            }

            mItemAdapter = new NavigationItemAdapter(title, icon, isHeader, count, colorSelected, removeSelector);
            mList.add(mItemAdapter);
        }
        return mList;
    }

}
