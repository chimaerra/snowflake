package snowflake.components.files.logviewer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DiskTableModel extends AbstractTableModel {
    private List<LineEntry> list = new ArrayList<>();
    private RandomAccessFile raf;

    public DiskTableModel(String file, List<LineEntry> list) throws FileNotFoundException {
        raf = new RandomAccessFile(file, "r");
        this.list = list;
    }

    public List<LineEntry> getList() {
        return list;
    }

    public void addLines(List<LineEntry> lines) {
        int index1 = list.size();
        list.addAll(lines);
        this.fireTableRowsInserted(index1, index1 + lines.size());
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LineEntry ent = list.get(rowIndex);
        byte[] b = new byte[ent.length];
        try {
            raf.seek(ent.offset);
            raf.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(b, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
