package com.dasunica.offroute.file_explorer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import com.dasunica.offroute.R;

import com.dasunica.offroute.Principal;
import com.dasunica.offroute.controllers.FileController;
import com.dasunica.offroute.utils.VariablesMobile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by fran on 5/10/14.
 */
public class FileChooserDialog{

    private static File startFolder;
    private VariablesMobile variablesMobile;

    private FileArrayAdapter fileArrayAdapter;
    private TextView tittleTextView;
    private Context context;
    private AlertDialog.Builder alertDialog;

    public FileChooserDialog(Context context){
        this.context = context;
        variablesMobile = VariablesMobile.getInstance();
    }

    private void updateListedDirectory(File parentFolder,String extension) {
        FileChooserDialog.startFolder = parentFolder;
        tittleTextView.setText(parentFolder.getAbsolutePath());
        File[] childrenFiles = parentFolder.listFiles(new ExtensionFilter(
                extension));
        ArrayList<File> fileNames;
        if (childrenFiles != null) {
            ArrayList<File> directories = new ArrayList<File>();
            ArrayList<File> files = new ArrayList<File>();

            for (int i = 0; i < childrenFiles.length; i++) {
                if (childrenFiles[i].isDirectory()) {
                    directories.add(childrenFiles[i]);
                } else {
                    files.add(childrenFiles[i]);
                }
            }
            Collections.sort(directories, new SortIgnoreCase());
            Collections.sort(files, new SortIgnoreCase());

            fileNames = new ArrayList<File>(childrenFiles.length);
            for (int i = 0; i < directories.size(); i++) {
                fileNames.add(directories.get(i));
            }

            for (int i = 0; i < files.size(); i++) {
                fileNames.add(files.get(i));
            }
        } else {
            fileNames = new ArrayList<File>();
        }
        if (fileArrayAdapter == null) {
            createNewArrayAdapter(fileNames);
        } else {
            setArrayAdapterData(fileNames);
        }
    }

    public void createFileChooserDialog(String extension) {
        if (FileChooserDialog.startFolder == null) {
            FileChooserDialog.startFolder =
                new File(Environment.getExternalStorageDirectory() + "/offroute/");
        }
        createFileChooserDialog(FileChooserDialog.startFolder,extension);
    }

    private void createFileChooserDialog(File startingFolder, final String extension) {
        this.alertDialog = new AlertDialog.Builder(context);

        LinearLayout tittleLayout = new LinearLayout(this.context);
        tittleLayout.setOrientation(LinearLayout.VERTICAL);
        tittleTextView = new TextView(this.context);
        tittleTextView.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        tittleTextView.setTextAppearance(this.context,
                android.R.style.TextAppearance_Large);
        tittleTextView.setTextColor(this.context.getResources().getColor(
                android.R.color.black));
        tittleTextView.setGravity(Gravity.CENTER_VERTICAL
                | Gravity.CENTER_HORIZONTAL);
        tittleLayout.addView(tittleTextView);

        ImageButton imgButton = new ImageButton(this.context);
        imgButton.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        imgButton.setImageResource(R.drawable.ic_folder);
        imgButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String currentDirPath = tittleTextView.getText().toString();
                System.out.println("currentDirPath=" + currentDirPath);
                File currentFile = new File(currentDirPath);
                File parentFile = currentFile.getParentFile();
                System.out.println("parentFile=" + parentFile);
                if (parentFile != null) {
                    updateListedDirectory(parentFile,extension);
                }
            }

        });
        tittleLayout.addView(imgButton);

        alertDialog.setCustomTitle(tittleLayout);

        updateListedDirectory(startFolder,extension);

        alertDialog.setSingleChoiceItems(fileArrayAdapter, -1,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final File selectedFile = fileArrayAdapter.getItem(which);
                        if (selectedFile.isDirectory()) {
                            updateListedDirectory(selectedFile,extension);
                        } else {
                            if (context instanceof Principal) {
                                final FileController principal = variablesMobile.getFileController();
                                Thread t = new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        principal.loadFile(selectedFile);
                                    }
                                });
                                t.start();

                                dialog.dismiss();
                            }
                        }
                    }
                });
        alertDialog.show();
    }

    private void createNewArrayAdapter(ArrayList<File> items) {
        this.fileArrayAdapter = new FileArrayAdapter(this.context,
                android.R.layout.select_dialog_item, android.R.id.text1, items);
    }

    private void setArrayAdapterData(ArrayList<File> data) {
        this.fileArrayAdapter.clear();
        for (int i = 0; i < data.size(); i++) {
            this.fileArrayAdapter.add(data.get(i));
        }

        this.fileArrayAdapter.notifyDataSetChanged();
    }
}
