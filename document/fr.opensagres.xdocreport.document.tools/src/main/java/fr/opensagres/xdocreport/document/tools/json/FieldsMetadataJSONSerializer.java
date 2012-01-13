package fr.opensagres.xdocreport.document.tools.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class FieldsMetadataJSONSerializer {

	private static final String LF = System.getProperty("line.separator");
	private static final String TAB = "\t";

	private static final FieldsMetadataJSONSerializer INSTANCE = new FieldsMetadataJSONSerializer();

	public static FieldsMetadataJSONSerializer getInstance() {
		return INSTANCE;
	}

	public void save(FieldsMetadata fieldsMetadata, Writer writer,
			boolean indent) throws IOException {
		save(fieldsMetadata, writer, null, indent);
	}

	public void save(FieldsMetadata fieldsMetadata, OutputStream out,
			boolean indent) throws IOException {
		save(fieldsMetadata, null, out, indent);
	}

	private void save(FieldsMetadata fieldsMetadata, Writer writer,
			OutputStream out, boolean indent) throws IOException {
		Collection<FieldMetadata> fields = fieldsMetadata.getFields();
		write("{", writer, out);

		boolean hasJSON = false;
		Map<String, Object> context = new LinkedHashMap<String, Object>();
		for (FieldMetadata field : fields) {

			String name = field.getFieldName();
			if (name.indexOf('.') == -1) {
				if (hasJSON) {
					write(",", writer, out);
				}
				writeJSON(name, name + "_Value", writer, out, indent);
				hasJSON = true;
			} else {

			}

			// save(field, writer, out, indent);
		}
		if (indent) {
			write(LF, writer, out);
		}
		write("}", writer, out);
	}

	private void writeJSON(String propertyName, String propertyValue,
			Writer writer, OutputStream out, boolean indent) throws IOException {
		write(LF, writer, out);
		if (indent) {
			write(TAB, writer, out);
		}
		write("{", writer, out);
		write(propertyName, writer, out);
		write(": \"", writer, out);
		write(propertyValue, writer, out);
		write("\"}", writer, out);
	}

	private void write(String s, Writer writer, OutputStream out)
			throws IOException {
		if (writer == null) {
			out.write(s.getBytes());
		} else {
			writer.write(s);
		}
	}

}
