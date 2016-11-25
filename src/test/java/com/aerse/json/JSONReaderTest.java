package com.aerse.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.aerse.json.Cursor;
import com.aerse.json.JSONReader;

public class JSONReaderTest {

	private Cursor cursor;
	private Reader reader;

	@Test
	public void testNullObject() throws Exception {
		reader = new StringReader("null");
		assertFalse(JSONReader.startObject(reader, cursor));
	}
	
	@Test
	public void testSkipEmptyArray() throws Exception {
		reader = new StringReader("{ \"data\": [], \"id\": 12}");
		JSONReader.startObject(reader, cursor);
		assertTag("data");
		JSONReader.skipValue(reader, cursor);
		assertTag("id");
		assertLong(12);
		JSONReader.endObject(reader, cursor);
	}

	@Test
	public void testEmptyArray() throws Exception {
		reader = new StringReader("[]");
		List<Long> result = JSONReader.readLongArray(reader, cursor);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testEmptyArray2() throws Exception {
		reader = new StringReader("[null]");
		List<Long> result = JSONReader.readLongArray(reader, cursor);
		assertEquals(1, result.size());
		assertNull(result.get(0));
	}

	@Test
	public void testDetectEndOfObjectByCallingNextTag() throws Exception {
		reader = getReaderFromClasspath("object.json");
		JSONReader.startObject(reader, cursor);
		assertTag("object1");
		JSONReader.startObject(reader, cursor);
		assertTag("id");
		JSONReader.skipValue(reader, cursor);
		assertNull(JSONReader.readTag(reader, cursor));
		JSONReader.endObject(reader, cursor);
		assertTag("object2");
	}

	@Test
	public void testDecodeComplete() throws Exception {
		reader = getReaderFromClasspath("decodecomplete.json");
		JSONReader.startObject(reader, cursor);
		assertTag("integer");
		assertInteger(19);
		assertTag("long");
		assertLong(19);
		assertTag("float");
		assertFloat(12.45f);
		assertTag("double");
		assertDouble(-34.45e-1);
		assertTag("string");
		assertString("\"\\/\b\f\n\r\t\u2022string");
		assertTag("boolean_true");
		assertBoolean(true);
		assertTag("boolean_false");
		assertBoolean(false);
		assertTag("object");
		JSONReader.startObject(reader, cursor);
		assertTag("number");
		assertInteger(19);
		assertTag("object_inner");
		JSONReader.startObject(reader, cursor);
		assertTag("number");
		assertInteger(19);
		assertTag("array_inner");
		assertArrayInteger(19, null);
		JSONReader.endObject(reader, cursor);
		JSONReader.endObject(reader, cursor);
		assertTag("array_integer");
		assertArrayInteger(19, null);
		assertTag("array_long");
		assertArrayLong(19l, null);
		assertTag("array_string");
		assertArrayString("\"test\"", null, "2");
		assertTag("array_object");
		JSONReader.startArray(reader, cursor);
		JSONReader.startObject(reader, cursor);
		assertTag("number");
		assertInteger(19);
		assertTag("object_inner");
		JSONReader.startObject(reader, cursor);
		assertTag("number");
		assertInteger(19);
		JSONReader.endObject(reader, cursor);
		JSONReader.endObject(reader, cursor);
		assertFalse(JSONReader.startObject(reader, cursor));
		JSONReader.endObject(reader, cursor);
		JSONReader.endArray(reader, cursor);
		assertTag("array_boolean");
		assertArrayBoolean(false, true, null);
		assertTag("array_array");
		JSONReader.startArray(reader, cursor);
		assertArrayInteger(19, null);
		assertTrue(JSONReader.readIntegerArray(reader, cursor).isEmpty());
		assertArrayString("1", null);
		JSONReader.endArray(reader, cursor);
		assertTag("integer_null");
		assertNull(JSONReader.readInteger(reader, cursor));
		assertTag("long_null");
		assertNull(JSONReader.readLong(reader, cursor));
		assertTag("float_null");
		assertNull(JSONReader.readFloat(reader, cursor));
		assertTag("double_null");
		assertNull(JSONReader.readDouble(reader, cursor));
		assertTag("string_null");
		assertNull(JSONReader.readString(reader, cursor));
		assertTag("boolean_null");
		assertNull(JSONReader.readBoolean(reader, cursor));
		assertTag("object_null");
		assertFalse(JSONReader.startObject(reader, cursor));
		assertTag("array_null");
		assertFalse(JSONReader.startArray(reader, cursor));
		JSONReader.endObject(reader, cursor);
	}

	@Test
	public void testSkipValue() throws Exception {
		reader = getReaderFromClasspath("decodecomplete.json");
		JSONReader.startObject(reader, cursor);
		assertTag("integer");
		JSONReader.skipValue(reader, cursor);
		assertTag("long");
		JSONReader.skipValue(reader, cursor);
		assertTag("float");
		JSONReader.skipValue(reader, cursor);
		assertTag("double");
		JSONReader.skipValue(reader, cursor);
		assertTag("string");
		JSONReader.skipValue(reader, cursor);
		assertTag("boolean_true");
		JSONReader.skipValue(reader, cursor);
		assertTag("boolean_false");
		JSONReader.skipValue(reader, cursor);
		assertTag("object");
		JSONReader.skipValue(reader, cursor);
		assertTag("array_integer");
		JSONReader.skipValue(reader, cursor);
		assertTag("array_long");
		JSONReader.skipValue(reader, cursor);
		assertTag("array_string");
		JSONReader.skipValue(reader, cursor);
		assertTag("array_object");
		JSONReader.skipValue(reader, cursor);
		assertTag("array_boolean");
		JSONReader.skipValue(reader, cursor);
		assertTag("array_array");
		JSONReader.skipValue(reader, cursor);
		assertTag("integer_null");
		JSONReader.skipValue(reader, cursor);
		assertTag("long_null");
		JSONReader.skipValue(reader, cursor);
		assertTag("float_null");
		JSONReader.skipValue(reader, cursor);
		assertTag("double_null");
		JSONReader.skipValue(reader, cursor);
		assertTag("string_null");
		JSONReader.skipValue(reader, cursor);
		assertTag("boolean_null");
		JSONReader.skipValue(reader, cursor);
		assertTag("object_null");
		JSONReader.skipValue(reader, cursor);
		assertTag("array_null");
		JSONReader.skipValue(reader, cursor);
		JSONReader.endObject(reader, cursor);
	}

	@Before
	public void start() {
		cursor = new Cursor();
	}

	private void assertArrayBoolean(Boolean... array) throws Exception {
		assertEquals(Arrays.asList(array), JSONReader.readBooleanArray(reader, cursor));
	}

	private void assertArrayString(String... array) throws Exception {
		assertEquals(Arrays.asList(array), JSONReader.readStringArray(reader, cursor));
	}

	private void assertArrayLong(Long... array) throws Exception {
		assertEquals(Arrays.asList(array), JSONReader.readLongArray(reader, cursor));
	}

	private void assertArrayInteger(Integer... array) throws Exception {
		assertEquals(Arrays.asList(array), JSONReader.readIntegerArray(reader, cursor));
	}

	private void assertBoolean(boolean bool) throws Exception {
		assertEquals(bool, JSONReader.readBoolean(reader, cursor));
	}

	private void assertString(String str) throws Exception {
		assertEquals(str, JSONReader.readString(reader, cursor));
	}

	private void assertDouble(double number) throws Exception {
		assertEquals(new Double(number), JSONReader.readDouble(reader, cursor), 0.0);
	}

	private void assertFloat(float number) throws Exception {
		assertEquals(new Float(number), JSONReader.readFloat(reader, cursor), 0.0);
	}

	private void assertInteger(int number) throws Exception {
		assertEquals(new Integer(number), JSONReader.readInteger(reader, cursor));
	}

	private void assertLong(long number) throws Exception {
		assertEquals(new Long(number), JSONReader.readLong(reader, cursor));
	}

	private void assertTag(String name) throws Exception {
		assertEquals(name, JSONReader.readTag(reader, cursor).toString());
	}

	private static Reader getReaderFromClasspath(String name) {
		InputStream is = JSONReader.class.getClassLoader().getResourceAsStream(name);
		if (is == null) {
			throw new IllegalArgumentException("unable to find: " + name + " in classpath");
		}
		return new InputStreamReader(is);
	}

}
