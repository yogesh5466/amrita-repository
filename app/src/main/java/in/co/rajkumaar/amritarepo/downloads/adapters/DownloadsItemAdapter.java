/*
 * Copyright (c) 2020 RAJKUMAR S
 */

package in.co.rajkumaar.amritarepo.downloads.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

import in.co.rajkumaar.amritarepo.R;
import in.co.rajkumaar.amritarepo.downloads.FolderHelper;
import in.co.rajkumaar.amritarepo.downloads.models.DownloadsItem;

public class DownloadsItemAdapter extends ArrayAdapter<DownloadsItem> {

    private final FolderHelper folderHelper;
    private File currentFile;

    public DownloadsItemAdapter(Context context, ArrayList<DownloadsItem> downloadsItems, FolderHelper folderHelper) {
        super(context, 0, downloadsItems);
        this.folderHelper = folderHelper;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.delete_list_item, parent, false);
        }

        final DownloadsItem current = getItem(position);

        if (current != null) {
            final boolean checkbox = current.getCheckBox();
            final String fileSize = current.getSize();

            final CheckBox checkBox = listItemView.findViewById(R.id.checkbox);
            TextView title = listItemView.findViewById(R.id.filename);
            TextView size = listItemView.findViewById(R.id.filesize);

            if (getContext().getString(R.string.go_back).equalsIgnoreCase(current.getFilePath())) {
                checkBox.setVisibility(View.INVISIBLE);
                size.setVisibility(View.GONE);
            } else {
                currentFile = new File(current.getFilePath());
                checkBox.setVisibility(View.VISIBLE);
                size.setVisibility(View.VISIBLE);
            }

            title.setText(currentFile != null ? currentFile.getName() : Html.fromHtml(current.getFilePath()));
            size.setText(fileSize);
            checkBox.setChecked(checkbox);

            RelativeLayout container = listItemView.findViewById(R.id.container);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getContext().getString(R.string.go_back).equalsIgnoreCase(current.getFilePath()) || new File(current.getFilePath()).isDirectory()) {
                        folderHelper.loadFilesFromDir(current.getFilePath());
                    } else {
                        checkBox.setChecked(!checkBox.isChecked());
                        current.setCheckBox(checkBox.isChecked());
                    }
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    current.setCheckBox(checkBox.isChecked());
                }
            });
        }
        return listItemView;
    }
}
