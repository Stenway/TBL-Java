package com.stenway.tbl;

import com.stenway.reliabletxt.ReliableTxtEncoding;
import com.stenway.sml.SmlDocument;
import java.util.ArrayList;
import java.util.Objects;

public class TblDocument {
	private ReliableTxtEncoding encoding = ReliableTxtEncoding.UTF_8;
	private final String[] columnNames;
	private final ArrayList<String[]> rows = new ArrayList<>();
	public final TblMetaData meta = new TblMetaData();
	
	public TblDocument(String... columnNames) {
		Objects.requireNonNull(columnNames);
		if (columnNames.length < 2) { throw new IllegalArgumentException("Table must have at least two columns"); }
		for (String columnName : columnNames) {
			if (columnName == null) { throw new IllegalArgumentException("Column name cannot be null"); }
		}
		this.columnNames = columnNames;
	}
	
	public String[] getColumnNames() {
		return columnNames.clone();
	}
	
	public final void setEncoding(ReliableTxtEncoding encoding) {
		Objects.requireNonNull(encoding);
		this.encoding = encoding;
	}
	
	public ReliableTxtEncoding getEncoding() {
		return encoding;
	}
	
	public void addRow(String... values) {
		if (values.length < 2) { throw new IllegalArgumentException("Row must have at least two values"); }
		if (values[0] == null) { throw new IllegalArgumentException("First row value cannot be null"); }
		if (values.length > columnNames.length) { throw new IllegalArgumentException("Row has more values than there are columns"); }
		rows.add(values);
	}
	
	public String[][] getRows() {
		return rows.toArray(new String[0][]);
	}
	
	public static TblDocument parse(String content) {
		SmlDocument smlDocument = SmlDocument.parse(content, false);
		return TblParser.parseElement(smlDocument.getRoot());
	}
}
