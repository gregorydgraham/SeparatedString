package nz.co.gregs.separatedstring;

import java.time.Instant;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

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
/**
 *
 * @author gregorygraham
 */
public class HTMLTableStringTest {

  public HTMLTableStringTest() {
  }
  
  @Test
  public void testSimpleTable(){
    HTMLTableString table = new HTMLTableString();
    table.addLine("1",2,3,4);
    String encode = table.encode();
    assertThat(encode, is("<table>\n<tr><td>1</td><td>2</td><td>3</td><td>4</td></tr>\n</table>\n"));
  }
  
  @Test
  public void testBlankRowTable(){
    HTMLTableString table = new HTMLTableString();
    table.addLine();
    table.addLine("1",2,3,4);
    String encode = table.encode();
    assertThat(encode, is("<table>\n<tr></tr>\n<tr><td>1</td><td>2</td><td>3</td><td>4</td></tr>\n</table>\n"));
  }
  
  @Test
  public void testNullValue(){
    HTMLTableString table = new HTMLTableString();
    table.addLine("1",2,3,4);
    table.addLine("Alice",null,"Cindy","Dorothy");
    table.withNullsAs("[NULL]");
    String encode = table.encode();
    assertThat(encode, is("<table>\n<tr><td>1</td><td>2</td><td>3</td><td>4</td></tr>\n<tr><td>Alice</td><td>[NULL]</td><td>Cindy</td><td>Dorothy</td></tr>\n</table>\n"));
  }
  
  @Test
  public void testIsEmpty(){
    HTMLTableString table = new HTMLTableString();
    assertThat(table.isEmpty(), is(true));
    table.addLine("1",2,3,4);
    assertThat(table.isEmpty(), is(false));
    table.addLine("Alice",null,"Cindy","Dorothy");
    assertThat(table.isEmpty(), is(false));
    table.withNullsAs("[NULL]");
    assertThat(table.isEmpty(), is(false));
  }
  
  @Test
  public void testIsNotEmpty(){
    HTMLTableString table = new HTMLTableString();
    assertThat(table.isNotEmpty(), is(false));
    table.addLine("1",2,3,4);
    assertThat(table.isNotEmpty(), is(true));
    table.addLine("Alice",null,"Cindy","Dorothy");
    assertThat(table.isNotEmpty(), is(true));
    table.withNullsAs("[NULL]");
    assertThat(table.isNotEmpty(), is(true));
  }
  
  @Test
  public void testRemoveAllStringArray(){
    HTMLTableString table = new HTMLTableString();
    assertThat(table.isNotEmpty(), is(false));
    table.addLine("1",2,3,4);
    assertThat(table.isNotEmpty(), is(true));
    table.addLine("Alice",null,"Cindy","Dorothy");
    assertThat(table.isNotEmpty(), is(true));
    System.out.println(table.encode());
    table.removeAll("1","2","3","4","5");
    assertThat(table.isNotEmpty(), is(true));
    System.out.println(table.encode());
    table.removeAll("Alice","","Cindy","Dorothy");
    assertThat(table.isNotEmpty(), is(false));
  }
  
  @Test
  public void testRemoveAllStringList(){
    HTMLTableString table = new HTMLTableString();
    assertThat(table.isNotEmpty(), is(false));
    table.addLine("1",2,3,4);
    assertThat(table.isNotEmpty(), is(true));
    table.addLine("Alice",null,"Cindy","Dorothy");
    assertThat(table.isNotEmpty(), is(true));
    System.out.println(table.encode());
    table.removeAll(List.of("1","2","3","4","5"));
    assertThat(table.isNotEmpty(), is(true));
    System.out.println(table.encode());
    table.removeAll(List.of("Alice","","Cindy","Dorothy"));
    assertThat(table.isNotEmpty(), is(false));
  }
  
  @Test
  public void testFormatting(){
    String ukFormat = DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.FULL, FormatStyle.MEDIUM, Chronology.ofLocale(Locale.UK), Locale.UK);
    DateTimeFormatter formatDates = DateTimeFormatter.ofPattern(ukFormat).withZone(ZoneId.systemDefault());
    Instant instant = Instant.parse("2020-02-15T18:35:24.00Z");
    
    HTMLTableString table = new HTMLTableString();
    table.setFormatFor(Instant.class, (date)->formatDates.format(date));
    table.addLine(instant,5,6,7);
    String encode = table.encode();
    assertThat(encode, is("<table>\n<tr><td>"+formatDates.format(instant)+"</td><td>5</td><td>6</td><td>7</td></tr>\n</table>\n"));
  }
  
  @Test
  public void testParseToLines(){
    HTMLTableString table = new HTMLTableString();
    final Object[] values1 = {"1",2,3,4};
    final String[] values2 = {"Alice",null,"Cindy","Dorothy"};
    Object[][] values = {values1, values2};
    table.addLine(values1);
    table.addLine(values2);
    table.withNullsAs("[NULL]");
    String encode = table.encode();
    assertThat(encode, is("<table>\n<tr><td>1</td><td>2</td><td>3</td><td>4</td></tr>\n<tr><td>Alice</td><td>[NULL]</td><td>Cindy</td><td>Dorothy</td></tr>\n</table>\n"));
    List<List<String>> lines = table.parseToLines(encode);
    for (int i = 0; i < values.length; i++) {
      Object[] value = values[i];
      List<String> line = lines.get(i);
      for (int j = 0; j < value.length; j++) {
        String val = (value[j]==null?"NULL":value[j].toString());
        String str = line.get(j);
        assertThat(str, anyOf(is(val),is(table.getNullRepresentation())));
      }
    }
    
  }
}
