package com.stenway.tbl;

import com.stenway.sml.SmlAttribute;
import com.stenway.sml.SmlElement;

public class TblParser {
	private static String[] combine(String name, String[] values) {
		String[] result = new String[values.length + 1];
		result[0] = name;
		System.arraycopy(values, 0, result, 1, values.length);
		return result;
	}
	
	private static void parseMeta(TblMetaData metaData, SmlElement metaElement) {
		metaData.title = getSingleStringOrNull(metaElement, "Title");
	}
	
	private static String getSingleStringOrNull(SmlElement element, String name) {
		if (element.hasAttribute(name)) {
			if (element.attributes(name).length > 1) { throw new IllegalArgumentException("Only one \""+name+"\" attribute allowed"); }
			SmlAttribute attribute = element.attribute(name);
			if (attribute.getValues().length > 1) { throw new IllegalArgumentException("Only one value in meta attribute\""+name+"\" allowed"); }
			return attribute.getString();
		} else {
			return null;
		}
	}
	
	public static TblDocument parseElement(SmlElement element) {
		if (!element.hasName("Table")) { throw new IllegalArgumentException("Not a valid table document"); }
		
		SmlAttribute[] attributes = element.attributes();
		if (attributes.length == 0) { throw new IllegalArgumentException("No column names"); }
		SmlAttribute columnNamesAttribute = attributes[0];
		for (String value : columnNamesAttribute.getValues()) {
			if (value == null) { throw new IllegalArgumentException("Column name cannot be null"); }
		}
		String[] columnNames = combine(columnNamesAttribute.getName(), columnNamesAttribute.getValues());
		TblDocument document = new TblDocument(columnNames);
		
		if (element.hasElement("Meta")) {
			if (element.elements().length > 1) { throw new IllegalArgumentException("Only one meta element is allowed"); }
			parseMeta(document.meta, element.element("Meta"));
		} else {
			if (element.elements().length > 0) { throw new IllegalArgumentException("Only meta element is allowed"); }
		}

		for (int i=1; i<attributes.length; i++) {
			SmlAttribute rowAttribute = attributes[i];
			String[] rowValues = combine(rowAttribute.getName(), rowAttribute.getValues());
			document.addRow(rowValues);
		}
		return document;
	}
}
