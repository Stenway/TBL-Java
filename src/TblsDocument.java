package com.stenway.tbl;

import com.stenway.reliabletxt.ReliableTxtEncoding;
import com.stenway.sml.SmlDocument;
import com.stenway.sml.SmlElement;
import java.util.ArrayList;
import java.util.Objects;

public class TblsDocument {
	private ReliableTxtEncoding encoding = ReliableTxtEncoding.UTF_8;
	
	public final ArrayList<TblDocument> tables = new ArrayList<>();
	
	public final void setEncoding(ReliableTxtEncoding encoding) {
		Objects.requireNonNull(encoding);
		this.encoding = encoding;
	}
	
	public ReliableTxtEncoding getEncoding() {
		return encoding;
	}
	
	public static TblsDocument parse(String content) {
		TblsDocument document = new TblsDocument();
		SmlDocument smlDocument = SmlDocument.parse(content, false);
		SmlElement rootElement = smlDocument.getRoot();
		if (!rootElement.hasName("Tables")) { throw new IllegalArgumentException("Not a valid tables document"); }
		for (SmlElement tableElement : rootElement.elements("Table")) {
			TblDocument tableDocument = TblParser.parseElement(tableElement);
			document.tables.add(tableDocument);
		}
		return document;
	}
}
