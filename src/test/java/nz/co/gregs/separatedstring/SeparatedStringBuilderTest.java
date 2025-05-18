/*
 * Copyright 2025 Gregory Graham.
 *
 * Commercial licenses are available, please contact info@gregs.co.nz for details.
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/ 
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * 
 * You are free to:
 *     Share - copy and redistribute the material in any medium or format
 *     Adapt - remix, transform, and build upon the material
 * 
 *     The licensor cannot revoke these freedoms as long as you follow the license terms.               
 *     Under the following terms:
 *                 
 *         Attribution - 
 *             You must give appropriate credit, provide a link to the license, and indicate if changes were made. 
 *             You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 *         NonCommercial - 
 *             You may not use the material for commercial purposes.
 *         ShareAlike - 
 *             If you remix, transform, or build upon the material, 
 *             you must distribute your contributions under the same license as the original.
 *         No additional restrictions - 
 *             You may not apply legal terms or technological measures that legally restrict others from doing anything the 
 *             license permits.
 * 
 * Check the Creative Commons website for any details, legalese, and updates.
 */
package nz.co.gregs.separatedstring;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

/**
 *
 * @author gregorygraham
 */
public class SeparatedStringBuilderTest {

  public SeparatedStringBuilderTest() {
  }

  @Test
  public void testHTMLOrderedList() {
    SeparatedString sep = SeparatedStringBuilder.htmlOrderedList();
    sep.add(1);
    sep.addAll("2", "3", "More");
    String encoded = sep.encode();
    assertThat(encoded, is("<ol>\n<li>1</li>\n<li>2</li>\n<li>3</li>\n<li>More</li>\n</ol>\n"));
  }

  @Test
  public void testHTMLUnorderedList() {
    SeparatedString sep = SeparatedStringBuilder.htmlUnorderedList();
    sep.add(1);
    sep.addAll("2", "3", "More");
    String encoded = sep.encode();
    assertThat(encoded, is("<ul>\n<li>1</li>\n<li>2</li>\n<li>3</li>\n<li>More</li>\n</ul>\n"));
  }

  @Test
  public void testMapEncoding() {

    SeparatedString extrasEncoder = SeparatedStringBuilder
            .forSeparator(";")
            .withKeyValueSeparator("=")
            .setFormatFor(Instant.class, d -> DATETIME_FORMAT.format(d))
            .withEscapeChar("!");
    
    String value = extrasEncoder.addAll(new HashMap<>(0)).encode();
    assertThat(value, is(""));
    final HashMap<String, Object> hashMap = new HashMap<>(0);
    hashMap.put("first", "1");
    hashMap.put("second", 2);
    hashMap.put("third", Instant.ofEpochMilli(0l));
    value = extrasEncoder.addAll(hashMap).encode();
    assertThat(value, is("third=1970-01-01 12:00;first=1;second=2"));
    extrasEncoder.removeAll(hashMap);
    extrasEncoder.addAll(extrasEncoder.parseToMap(value));
    assertThat(extrasEncoder.encode(), is("third=1970-01-01 12:00;first=1;second=2"));
    

  }
  private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());


  @Test
  public void testMapEncodingWithIncludedKeyValueSeparator() {

    SeparatedString extrasEncoder = SeparatedStringBuilder
            .forSeparator(";")
            .setFormatFor(Instant.class, d -> DATETIME_FORMAT.format(d))
            .withEscapeChar("!");
    
    String value = extrasEncoder.addAll(new HashMap<>(0)).encode();
    assertThat(value, is(""));
    final HashMap<String, Object> hashMap = new HashMap<>(0);
    hashMap.put("first", "1");
    hashMap.put("second", 2);
    hashMap.put("third", Instant.ofEpochMilli(0l));
    value = extrasEncoder.addAll(hashMap,"=").encode();
    assertThat(value, is("third=1970-01-01 12:00;first=1;second=2"));
    extrasEncoder.removeAll(hashMap);
    extrasEncoder.addAll(extrasEncoder.parseToMap(value));
    assertThat(extrasEncoder.encode(), is("third=1970-01-01 12:00;first=1;second=2"));
    

  }
  @Test
  public void testEncodingStressTest() {
    SeparatedString clusterHostEncoder = SeparatedStringBuilder
            .forSeparator("|")
            .withPrefix("<")
            .withSuffix(">")
            .withEscapeChar("!");
    String clusterHostsEncoded = clusterHostEncoder.addAll(new ArrayList<>(0)).encode();

    SeparatedString extrasEncoder = SeparatedStringBuilder
            .forSeparator(";")
            .withKeyValueSeparator("=")
            .withEscapeChar("!");
    String extrasEncoded = extrasEncoder.addAll(new HashMap<>(0)).encode();

    SeparatedString mainEncoder = SeparatedStringBuilder.forSeparator(", ").withEscapeChar("\\").withPrefix("DATABASECONNECTIONSETTINGS: ");
    mainEncoder.addAll(
            "nz.co.gregs.dbvolution.databases.H2MemoryDB",
            "localhost",
            "9123",
            "",
            "unknown",
            "",
            "",
            "",
            "",
            "soloDB2",
            "",
            clusterHostsEncoded,
            extrasEncoded
    );
    String value = mainEncoder.encode();
    assertThat(value, is("DATABASECONNECTIONSETTINGS: nz.co.gregs.dbvolution.databases.H2MemoryDB, localhost, 9123, , unknown, , , , , soloDB2, , , "));
  }
}
