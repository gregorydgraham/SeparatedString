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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

/**
 *
 * @author Gregory Graham
 */
public class DecoderTest {

  public DecoderTest() {
  }

  @Test
  public void testDecoder() {
    Builder builder = Builder.go();

    assertThat(builder, notNullValue(Builder.class));

    SeparatedString separatedString = builder.getSeparatedString();

    assertThat(separatedString.getEmptyValue(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
    assertThat(separatedString.getKeyValueSeparator(), is(""));
    assertThat(separatedString.getLineEnd(), is(""));
    assertThat(separatedString.getLineStart(), is(""));
    assertThat(separatedString.getNullRepresentation(), is("null"));
    assertThat(separatedString.getPrefix(), is(""));
    assertThat(separatedString.getSeparator(), is(" "));
    assertThat(separatedString.getSuffix(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.isClosedLoop(), is(false));
    assertThat(separatedString.isEmpty(), is(true));
    assertThat(separatedString.isNotLoop(), is(true));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isRetainingNulls(), is(false));
    assertThat(separatedString.isTrimBlanks(), is(false));
    assertThat(separatedString.isUniqueValuesOnly(), is(false));

    Decoder decoder = builder.decoder();
    assertThat(decoder, notNullValue(Decoder.class));

    separatedString = decoder.getSeparatedString();

    assertThat(separatedString.getEmptyValue(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
    assertThat(separatedString.getKeyValueSeparator(), is(""));
    assertThat(separatedString.getLineEnd(), is(""));
    assertThat(separatedString.getLineStart(), is(""));
    assertThat(separatedString.getNullRepresentation(), is("null"));
    assertThat(separatedString.getPrefix(), is(""));
    assertThat(separatedString.getSeparator(), is(" "));
    assertThat(separatedString.getSuffix(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.isClosedLoop(), is(false));
    assertThat(separatedString.isEmpty(), is(true));
    assertThat(separatedString.isNotLoop(), is(true));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isRetainingNulls(), is(false));
    assertThat(separatedString.isTrimBlanks(), is(false));
    assertThat(separatedString.isUniqueValuesOnly(), is(false));
  }

  @Test
  public void testBuilder() {
    Builder bild = Builder.go();

    Decoder decoder = bild.decoder();
    assertThat(decoder, notNullValue(Decoder.class));

    SeparatedString separatedString = decoder.getSeparatedString();

    assertThat(separatedString.getEmptyValue(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
    assertThat(separatedString.getKeyValueSeparator(), is(""));
    assertThat(separatedString.getLineEnd(), is(""));
    assertThat(separatedString.getLineStart(), is(""));
    assertThat(separatedString.getNullRepresentation(), is("null"));
    assertThat(separatedString.getPrefix(), is(""));
    assertThat(separatedString.getSeparator(), is(" "));
    assertThat(separatedString.getSuffix(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.isClosedLoop(), is(false));
    assertThat(separatedString.isEmpty(), is(true));
    assertThat(separatedString.isNotLoop(), is(true));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isRetainingNulls(), is(false));
    assertThat(separatedString.isTrimBlanks(), is(false));
    assertThat(separatedString.isUniqueValuesOnly(), is(false));

    Builder builder = decoder.builder();

    separatedString = builder.getSeparatedString();

    assertThat(separatedString.getEmptyValue(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
    assertThat(separatedString.getKeyValueSeparator(), is(""));
    assertThat(separatedString.getLineEnd(), is(""));
    assertThat(separatedString.getLineStart(), is(""));
    assertThat(separatedString.getNullRepresentation(), is("null"));
    assertThat(separatedString.getPrefix(), is(""));
    assertThat(separatedString.getSeparator(), is(" "));
    assertThat(separatedString.getSuffix(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.isClosedLoop(), is(false));
    assertThat(separatedString.isEmpty(), is(true));
    assertThat(separatedString.isNotLoop(), is(true));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isRetainingNulls(), is(false));
    assertThat(separatedString.isTrimBlanks(), is(false));
    assertThat(separatedString.isUniqueValuesOnly(), is(false));
  }

  @Test
  public void testEncoder() {
    Builder bild = Builder.go();

    Decoder decoder = bild.decoder();
    assertThat(decoder, notNullValue(Decoder.class));

    SeparatedString separatedString = decoder.getSeparatedString();

    assertThat(separatedString.getEmptyValue(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
    assertThat(separatedString.getKeyValueSeparator(), is(""));
    assertThat(separatedString.getLineEnd(), is(""));
    assertThat(separatedString.getLineStart(), is(""));
    assertThat(separatedString.getNullRepresentation(), is("null"));
    assertThat(separatedString.getPrefix(), is(""));
    assertThat(separatedString.getSeparator(), is(" "));
    assertThat(separatedString.getSuffix(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.isClosedLoop(), is(false));
    assertThat(separatedString.isEmpty(), is(true));
    assertThat(separatedString.isNotLoop(), is(true));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isRetainingNulls(), is(false));
    assertThat(separatedString.isTrimBlanks(), is(false));
    assertThat(separatedString.isUniqueValuesOnly(), is(false));

    Encoder encoder = decoder.encoder();

    separatedString = encoder.getSeparatedString();

    assertThat(separatedString.getEmptyValue(), is(""));
    assertThat(separatedString.getEscapeChar(), is(""));
    assertThat(separatedString.getKeyValueSeparator(), is(""));
    assertThat(separatedString.getLineEnd(), is(""));
    assertThat(separatedString.getLineStart(), is(""));
    assertThat(separatedString.getNullRepresentation(), is("null"));
    assertThat(separatedString.getPrefix(), is(""));
    assertThat(separatedString.getSeparator(), is(" "));
    assertThat(separatedString.getSuffix(), is(""));
    assertThat(separatedString.getWrapAfter(), is(""));
    assertThat(separatedString.getWrapBefore(), is(""));
    assertThat(separatedString.isClosedLoop(), is(false));
    assertThat(separatedString.isEmpty(), is(true));
    assertThat(separatedString.isNotLoop(), is(true));
    assertThat(separatedString.isOpenLoop(), is(false));
    assertThat(separatedString.isRetainingNulls(), is(false));
    assertThat(separatedString.isTrimBlanks(), is(false));
    assertThat(separatedString.isUniqueValuesOnly(), is(false));
  }

  @Test
  public void testAddAllIntList() {
    Encoder encoder = Builder.start().separatedBy(",").withBlanksTrimmed().encoder();
    SeparatedString separatedString = encoder.getSeparatedString();
    assertThat(separatedString.isTrimBlanks(), is(true));
    encoder.addAll(0, List.of("blart", "blort   ", "    blurt  "));
    assertThat(encoder.encode(), is("blart,blort,blurt"));
    encoder.addAll(1, List.of("blert   ", "blirt", "  middle  of list"));
    assertThat(encoder.encode(), is("blart,blert,blirt,middle  of list,blort,blurt"));

    final Decoder decoder = encoder.decoder();

    assertThat(decoder.decode(encoder.encode()), is(List.of("blart", "blert", "blirt", "middle  of list", "blort", "blurt")));
  }

  @Test
  public void testDecodeToList() {
    Encoder encoder = Builder.start().separatedBy(",").withBlanksTrimmed().encoder();
    SeparatedString separatedString = encoder.getSeparatedString();
    assertThat(separatedString.isTrimBlanks(), is(true));
    assertThat(encoder.encode("blart", "blort   ", "    blurt  "), is("blart,blort,blurt"));
    assertThat(encoder.encode("blert   ", "blirt", "  end  of list"), is("blert,blirt,end  of list"));

    final Decoder decoder = encoder.decoder();

    assertThat(decoder.decodeToList(encoder.encode("blart", "blort   ", "    blurt  ")), is(List.of("blart", "blort", "blurt")));
    assertThat(decoder.decodeToList(encoder.encode("blert   ", "blirt", "  end  of list")), is(List.of("blert", "blirt", "end  of list")));
  }

  @Test
  public void testDecodeToLines() {
    final Builder builder = Builder.start().separatedBy(",").withBlanksTrimmed().withLineEndSequence("<\n").withLineStartSequence("!");
    Encoder encoder = builder.encoder();
    SeparatedString separatedString = encoder.getSeparatedString();
    assertThat(separatedString.isTrimBlanks(), is(true));
    String[] firstLine = {"blart", "blort   ", "    blurt  "};
    String[] secondLine = {"blert   ", "blirt", "  end  of list"};
    assertThat(encoder.encode(firstLine), is("blart,blort,blurt"));
    assertThat(encoder.encode(secondLine), is("blert,blirt,end  of list"));

    final Decoder decoder = encoder.decoder();
    // check the simplest case
    assertThat(decoder.decodeToLines(encoder.encode()), is(List.of()));

    encoder.addLine(firstLine[0], firstLine[1], firstLine[2]);
    encoder.addAll(secondLine[0], secondLine[1], secondLine[2]);

    final String encode = encoder.encode();
    List<List<String>> decoded = decoder.decodeToLines(encode);
    assertThat(decoded, is(List.of(List.of("blart", "blort", "blurt"), List.of("blert","blirt","end  of list"))));
  }

  @Test
  public void testDecodeToArrayString() {
    Encoder encoder = Builder.start().withOnlyUniqueValues().withKeyValueSeparator("=").encoder();
    encoder.add("a", "1");
    encoder.add("b", "2");
    SeparatedString separatedString = encoder.getSeparatedString();
    assertThat(separatedString.isUniqueValuesOnly(), is(true));
    assertThat(encoder.encode(), is("a=1 b=2"));

    encoder = Builder.start().withOnlyUniqueValues().withKeyValueSeparator("=").encoder();
    encoder.add("a", "1");
    encoder.add("b", "2");
    encoder.add("a", "1");
    encoder.add("b", "2");
    separatedString = encoder.getSeparatedString();
    assertThat(separatedString.isUniqueValuesOnly(), is(true));
    assertThat(encoder.encode(), is("a=1 b=2"));
    
    Decoder decoder = encoder.decoder();
    // check the simplest case
    assertThat(decoder.decodeToArray(""), is(new String[0]));
    
    final String encoded = encoder.encode();
    final String[] expectedArray = List.of("a=1","b=2").toArray(new String[0]);
    final String[] actualArray = decoder.decodeToArray(encoded);
    
    assertThat(actualArray, is(expectedArray));
  }

  @Test
  public void testDecodeToMap() {
    Encoder encoder = Builder.start().withKeyValueSeparator("~").encoder();
    SeparatedString separatedString = encoder.getSeparatedString();
    assertThat(separatedString.getKeyValueSeparator(), is("~"));
    Map<String, Object> map = new HashMap<>();
    map.put("a", 1);
    map.put("b", 2.3);
    map.put("c", Instant.EPOCH);
    map.put("d", ":>");
    encoder.addAllObjectMap(map);
    assertThat(encoder.encode(), is("a~1 b~2.3 c~1970-01-01T00:00:00Z d~:>"));
    
    Decoder decoder = encoder.decoder();
    // test the simplest case
    assertThat(decoder.decodeToMap("").isEmpty(), is(true));
    
    
    final Map<String, String> decodeToMap = decoder.decodeToMap(encoder.encode());
    
    assertThat(decodeToMap.size(), is(map.size()));
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      String key = entry.getKey();
      assertThat(decodeToMap.containsKey(key), is(true));
      Object value = entry.getValue();
      assertThat(decodeToMap.get(key).toString(), is(value.toString())); 
    }
    
    
  }

//  @Test
//  public void testAddAll() {
//    Encoder encoder = Builder.start().separatedBy("~").encoder();
//    SeparatedString separatedString = encoder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is("~"));
//    encoder.addAll(List.of("blart", "blort   ", "    blurt  "));
//    assertThat(encoder.encode(), is("blart~blort   ~    blurt  "));
//    encoder.addAll(List.of("blert   ", "blirt", "  end  of list"));
//    assertThat(encoder.encode(), is("blart~blort   ~    blurt  ~blert   ~blirt~  end  of list"));
//  }
//
//  @Test
//  public void testAddStringObject() {
//    Encoder encoder = Builder.start().withEscapeChar("~").withKeyValueSeparator(":>").encoder();
//    SeparatedString separatedString = encoder.getSeparatedString();
//    assertThat(separatedString.getEscapeChar(), is("~"));
//    encoder.add("a", 1);
//    encoder.add("b", 2.3);
//    encoder.add("c", Instant.EPOCH);
//    encoder.add("d", ":>");
//    assertThat(encoder.encode(), is("a:>1 b:>2.3 c:>1970-01-01T00:00:00Z d:>~:>"));
//  }
//
//  @Test
//  public void testAddAllMap() {
//    Encoder encoder = Builder.start().withKeyValueSeparator("~").encoder();
//    SeparatedString separatedString = encoder.getSeparatedString();
//    assertThat(separatedString.getKeyValueSeparator(), is("~"));
//    Map<String, Object> map = new HashMap<>();
//    map.put("a", 1);
//    map.put("b", 2.3);
//    map.put("c", Instant.EPOCH);
//    map.put("d", ":>");
//    encoder.addAll(map);
//    assertThat(encoder.encode(), is("a~1 b~2.3 c~1970-01-01T00:00:00Z d~:>"));
//  }
//
//  @Test
//  public void testAddLineStringArray() {
//    DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH24:mm").withZone(ZoneOffset.UTC);
//    final Function<Instant, String> formatter = d -> DATETIME_FORMAT.format(d);
//    Encoder encoder = Builder.start().setFormatFor(Instant.class, formatter).encoder();
//    SeparatedString separatedString = encoder.getSeparatedString();
//    assertThat(separatedString.getFormatterFor(Instant.now()), is(formatter));
//    encoder
//            .addLine("a", "b", "c", "d", "e")
//            .addLine("a", "b", "c", "d", "e")
//            .addLine()
//            .addLine("1", "2", "3");
//    assertThat(encoder.encode(), is("a b c d e\na b c d e\n\n1 2 3\n"));
//  }
//
//  @Test
//  public void testAddLine() {
//    DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH24:mm").withZone(ZoneOffset.UTC);
//    final Function<Instant, String> formatter = d -> DATETIME_FORMAT.format(d);
//    Encoder encoder = Builder.start().setFormatFor(Instant.class, formatter).encoder();
//    SeparatedString separatedString = encoder.getSeparatedString();
//    assertThat(separatedString.getFormatterFor(Instant.now()), is(formatter));
//    encoder
//            .addLine("a", "b", "c", "d", "e")
//            .addLine("a", "b", "c", "d", "e")
//            .addLine()
//            .addLine("1", "2", "3");
//    assertThat(encoder.encode(), is("a b c d e\na b c d e\n\n1 2 3\n"));
//  }
//
//  @Test
//  public void testAddAllFunctionList() {
//    DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mmZ").withZone(ZoneOffset.UTC);
//    final Function<Instant, String> formatter = d -> DATETIME_FORMAT.format(d);
//    Encoder encoder = Builder.start().separatedBy(",").withBlanksTrimmed().encoder();
//    SeparatedString separatedString = encoder.getSeparatedString();
//    assertThat(separatedString.isTrimBlanks(), is(true));
//    List<Instant> instants = List.of(Instant.EPOCH, Instant.EPOCH.plusSeconds(1000000));
//    encoder.addAll(formatter, instants);
//    assertThat(encoder.encode(), is("1970-01-01 00:00+0000,1970-01-12 13:46+0000"));
//  }
//
//  @Test
//  public void testAddAllFunctionArray() {
//    DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mmZ").withZone(ZoneOffset.UTC);
//    final Function<Instant, String> formatter = d -> DATETIME_FORMAT.format(d);
//    Encoder encoder = Builder.start().separatedBy(",").withBlanksTrimmed().encoder();
//    SeparatedString separatedString = encoder.getSeparatedString();
//    assertThat(separatedString.isTrimBlanks(), is(true));
//    encoder.addAll(formatter, Instant.EPOCH, Instant.EPOCH.plusSeconds(1000000));
//    assertThat(encoder.encode(), is("1970-01-01 00:00+0000,1970-01-12 13:46+0000"));
//  }
//
//  @Test
//  public void testAddIntString() {
//    Encoder encoder = Builder.start().withNullsAs("~").encoder();
//    SeparatedString separatedString = encoder.getSeparatedString();
//    assertThat(separatedString.getNullRepresentation(), is("~"));
//    encoder.add(0, "blert");
//    encoder.add(0, "blart");
//    encoder.add(2, "blort");
//    encoder.add(2, "blirt");
//    encoder.add(4, "blurt");
//    encoder.add(5, null);
//    assertThat(encoder.encode(), is("blart blert blirt blort blurt ~"));
//  }
//
//  @Test
//  public void testAddIntObject() {
//    Builder builder = Builder.start();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.isNotLoop(), is(true));
//    assertThat(separatedString.isOpenLoop(), is(false));
//    assertThat(separatedString.isClosedLoop(), is(false));
//
//    builder = Builder.start();
//    builder.withClosedLoop();
//    builder.withNoLoop();
//    separatedString = builder.getSeparatedString();
//    assertThat(separatedString.isNotLoop(), is(true));
//    assertThat(separatedString.isOpenLoop(), is(false));
//    assertThat(separatedString.isClosedLoop(), is(false));
//
//    Encoder encoder = builder.encoder();
//    encoder.add(0, 1);
//    encoder.add(0, Instant.EPOCH);
//    encoder.add(2, Color.RED);
//    assertThat(encoder.encode(), is("1970-01-01T00:00:00Z 1 java.awt.Color[r=255,g=0,b=0]"));
//    encoder.add(2, Color.RED);
//    assertThat(encoder.encode(), is("1970-01-01T00:00:00Z 1 java.awt.Color[r=255,g=0,b=0] java.awt.Color[r=255,g=0,b=0]"));
//  }
//
//  @Test
//  public void testAddString() {
//    Builder builder = Builder.start();
//    builder.withOpenLoop();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.isNotLoop(), is(false));
//    assertThat(separatedString.isOpenLoop(), is(true));
//    assertThat(separatedString.isClosedLoop(), is(false));
//
//    Encoder encoder = builder.encoder();
//    encoder.add("Anna");
//    encoder.add("Bob");
//    encoder.add("Carmen");
//    assertThat(encoder.encode(), is("Anna Bob Carmen"));
//    encoder.add("Anna");
//    assertThat(encoder.encode(), is("Anna Bob Carmen"));
//    encoder.add("Dave");
//    assertThat(encoder.encode(), is("Anna Bob Carmen Anna Dave"));
//  }
//
//  @Test
//  public void testAddObject() {
//    Builder builder = Builder.go();
//    builder.withClosedLoop();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.isNotLoop(), is(false));
//    assertThat(separatedString.isOpenLoop(), is(false));
//    assertThat(separatedString.isClosedLoop(), is(true));
//
//    Encoder encoder = builder.encoder();
//    encoder.add(Color.RED);
//    encoder.add(Color.GREEN);
//    encoder.add(Color.BLUE);
//    assertThat(encoder.encode(), is("java.awt.Color[r=255,g=0,b=0] java.awt.Color[r=0,g=255,b=0] java.awt.Color[r=0,g=0,b=255] java.awt.Color[r=255,g=0,b=0]"));
//    encoder.add(Color.RED);
//    assertThat(encoder.encode(), is("java.awt.Color[r=255,g=0,b=0] java.awt.Color[r=0,g=255,b=0] java.awt.Color[r=0,g=0,b=255] java.awt.Color[r=255,g=0,b=0]"));
//    encoder.add(Color.BLACK);
//    assertThat(encoder.encode(), is("java.awt.Color[r=255,g=0,b=0] java.awt.Color[r=0,g=255,b=0] java.awt.Color[r=0,g=0,b=255] java.awt.Color[r=255,g=0,b=0] java.awt.Color[r=0,g=0,b=0] java.awt.Color[r=255,g=0,b=0]"));
//  }
//
//  @Test
//  public void testContainingObjectArray() {
//    Builder builder = Builder.start();
//    builder.withEachTermPrecededAndFollowedWith("~");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getWrapBefore(), is("~"));
//    assertThat(separatedString.getWrapAfter(), is("~"));
//
//    Encoder encoder = builder.encoder();
//    encoder.containing(Color.RED, Color.GREEN, Color.BLUE);
//    assertThat(encoder.encode(), is("~java.awt.Color[r=255,g=0,b=0]~ ~java.awt.Color[r=0,g=255,b=0]~ ~java.awt.Color[r=0,g=0,b=255]~"));
//    encoder.containing(Color.RED);
//    assertThat(encoder.encode(), is("~java.awt.Color[r=255,g=0,b=0]~ ~java.awt.Color[r=0,g=255,b=0]~ ~java.awt.Color[r=0,g=0,b=255]~ ~java.awt.Color[r=255,g=0,b=0]~"));
//    encoder.containing(Color.BLACK);
//    assertThat(encoder.encode(), is("~java.awt.Color[r=255,g=0,b=0]~ ~java.awt.Color[r=0,g=255,b=0]~ ~java.awt.Color[r=0,g=0,b=255]~ ~java.awt.Color[r=255,g=0,b=0]~ ~java.awt.Color[r=0,g=0,b=0]~"));
//  }
//
//  @Test
//  public void testIsNotEmpty() {
//    Builder builder = Builder.start();
//    builder.withEachTermWrappedWith("~", "!");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getWrapBefore(), is("~"));
//    assertThat(separatedString.getWrapAfter(), is("!"));
//
//    Encoder encoder = builder.encoder();
//    assertThat(encoder.isNotEmpty(), is(false));
//    encoder.containing(Color.RED, Color.GREEN, Color.BLUE);
//    assertThat(encoder.isNotEmpty(), is(true));
//    assertThat(encoder.encode(), is("~java.awt.Color[r=255,g=0,b=0]! ~java.awt.Color[r=0,g=255,b=0]! ~java.awt.Color[r=0,g=0,b=255]!"));
//
//  }
//
//  @Test
//  public void testIsEmpty() {
//    Builder builder = Builder.start();
//    builder.withThisBeforeEachTerm("~");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getWrapBefore(), is("~"));
//
//    Encoder encoder = builder.encoder();
//    assertThat(encoder.isEmpty(), is(true));
//    encoder.containing(Color.RED, Color.GREEN, Color.BLUE);
//    assertThat(encoder.isEmpty(), is(false));
//    assertThat(encoder.encode(), is("~java.awt.Color[r=255,g=0,b=0] ~java.awt.Color[r=0,g=255,b=0] ~java.awt.Color[r=0,g=0,b=255]"));
//
//  }
//
//  @Test
//  public void testRemoveAllStringArray() {
//    Builder builder = Builder.start();
//    builder.withThisAfterEachTerm("~");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getWrapAfter(), is("~"));
//
//    Encoder encoder = builder.encoder();
//    encoder.containing(Color.RED, Color.GREEN, Color.BLUE);
//    assertThat(encoder.encode(), is("java.awt.Color[r=255,g=0,b=0]~ java.awt.Color[r=0,g=255,b=0]~ java.awt.Color[r=0,g=0,b=255]~"));
//    encoder.removeAll("java.awt.Color[r=255,g=0,b=0]", "java.awt.Color[r=0,g=255,b=0]", "java.awt.Color[r=0,g=0,b=255]");
//    assertThat(encoder.encode(), is(""));
//  }
//
//  @Test
//  public void testRemoveAllList() {
//    Builder builder = Builder.start();
//    builder.withPrefix("~");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getPrefix(), is("~"));
//
//    Encoder encoder = builder.encoder();
//    encoder.containing(Color.RED, Color.GREEN, Color.BLUE);
//    assertThat(encoder.encode(), is("~java.awt.Color[r=255,g=0,b=0] java.awt.Color[r=0,g=255,b=0] java.awt.Color[r=0,g=0,b=255]"));
//    encoder.removeAll(List.of(Color.RED, Color.GREEN, Color.BLUE));
//    assertThat(encoder.encode(), is(""));
//
//    encoder.containing(Color.RED, Color.GREEN, Color.BLUE);
//    assertThat(encoder.encode(), is("~java.awt.Color[r=255,g=0,b=0] java.awt.Color[r=0,g=255,b=0] java.awt.Color[r=0,g=0,b=255]"));
//    encoder.removeAll(List.of("java.awt.Color[r=255,g=0,b=0]", "java.awt.Color[r=0,g=255,b=0]","java.awt.Color[r=0,g=0,b=255]"));
//    assertThat(encoder.encode(),is(""));
//  }
//
//  @Test
//  public void testRemoveAllMap() {
//    Builder builder = Builder.start();
//    builder.withSuffix("!");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSuffix(), is("!"));
//    
//    Encoder encoder = builder.encoder();
//    Map<String, Object> map = new HashMap<>();
//    map.put("red", Color.RED);
//    map.put("green", Color.GREEN);
//    map.put("blue", Color.BLUE);
//    encoder.addAll(map);
//    assertThat(encoder.encode(), is("redjava.awt.Color[r=255,g=0,b=0] greenjava.awt.Color[r=0,g=255,b=0] bluejava.awt.Color[r=0,g=0,b=255]!"));
//    encoder.removeAll(new HashMap<>(0));
//    assertThat(encoder.encode(), is("redjava.awt.Color[r=255,g=0,b=0] greenjava.awt.Color[r=0,g=255,b=0] bluejava.awt.Color[r=0,g=0,b=255]!"));
//    map.remove("green");
//    encoder.removeAll(map);
//    assertThat(encoder.encode(), is("greenjava.awt.Color[r=0,g=255,b=0]!"));
//    map.put("green", Color.GREEN);
//    encoder.removeAll(map);
//    assertThat(encoder.encode(), is(""));
//  }
//
//  @Test
//  public void testRemoveInt() {
//    Builder builder = Builder.start();
//    builder.withNullsRetained(true);
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.isRetainingNulls(), is(true));
//    
//    Encoder encoder = builder.encoder();
//    encoder.addAll("red", "green", "blue");
//    assertThat(encoder.encode(), is("red green blue"));
//    encoder.remove(0);
//    assertThat(encoder.encode(), is("green blue"));
//    encoder.remove(1);
//    assertThat(encoder.encode(), is("green"));
//    encoder.remove(1);
//    assertThat(encoder.encode(), is("green"));
//    encoder.remove(0);
//    assertThat(encoder.encode(), is(""));
//  }
//
//  @Test
//  public void testUseWhenEmpty() {
//    Builder builder = Builder.start();
//    builder.useWhenEmpty("~");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getEmptyValue(), is("~"));
//  }
//
//  @Test
//  public void testWithLineEndSequence() {
//    Builder builder = Builder.start();
//    builder.withLineEndSequence("~");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getLineEnd(), is("~"));
//  }
//
//  @Test
//  public void testWithLineStartSequence() {
//    Builder builder = Builder.start();
//    builder.withLineStartSequence("~");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getLineStart(), is("~"));
//  }
//
//  @Test
//  public void testCSV() {
//    Builder csv = Builder.csv();
//    SeparatedString separatedString = csv.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is(", "));
//    assertThat(separatedString.getWrapBefore(), is("\""));
//    assertThat(separatedString.getWrapAfter(), is("\""));
//    assertThat(separatedString.getEscapeChar(), is("\\"));
//    assertThat(separatedString.getKeyValueSeparator(), is("="));
//  }
//
//  @Test
//  public void testTSV() {
//    Builder tsv = Builder.tsv();
//    SeparatedString separatedString = tsv.getSeparatedString();
//
//    assertThat(separatedString.getSeparator(), is("\t"));
//    assertThat(separatedString.getWrapBefore(), is("\""));
//    assertThat(separatedString.getWrapAfter(), is("\""));
//    assertThat(separatedString.getEscapeChar(), is("\\"));
//    assertThat(separatedString.getKeyValueSeparator(), is("="));
//  }
//
//  @Test
//  public void testHtmlOrderedList() {
//    Builder tsv = Builder.htmlOrderedList();
//    SeparatedString separatedString = tsv.getSeparatedString();
//
//    assertThat(separatedString.getSeparator(), is("\n"));
//    assertThat(separatedString.getWrapBefore(), is("<li>"));
//    assertThat(separatedString.getWrapAfter(), is("</li>"));
//    assertThat(separatedString.getPrefix(), is("<ol>\n"));
//    assertThat(separatedString.getSuffix(), is("\n</ol>\n"));
//  }
//
//  @Test
//  public void testHtmlUnorderedList() {
//    Builder tsv = Builder.htmlUnorderedList();
//    SeparatedString separatedString = tsv.getSeparatedString();
//
//    assertThat(separatedString.getSeparator(), is("\n"));
//    assertThat(separatedString.getWrapBefore(), is("<li>"));
//    assertThat(separatedString.getWrapAfter(), is("</li>"));
//    assertThat(separatedString.getPrefix(), is("<ul>\n"));
//    assertThat(separatedString.getSuffix(), is("\n</ul>\n"));
//  }
//
//  @Test
//  public void testStartsWith() {
//    Builder builder = Builder.startsWith("~");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getPrefix(), is("~"));
//  }
//
//  @Test
//  public void testForSeparator() {
//    Builder builder = Builder.forSeparator("~");
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is("~"));
//  }
//
//  @Test
//  public void testBySpaces() {
//    Builder builder = Builder.bySpaces();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is(" "));
//  }
//
//  @Test
//  public void testByCommas() {
//    Builder builder = Builder.byCommas();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is(","));
//  }
//
//  @Test
//  public void testByCommaSpace() {
//    Builder builder = Builder.byCommaSpace();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is(", "));
//  }
//
//  @Test
//  public void testByCommasWithQuotedTermsAndBackslashEscape() {
//    Builder builder = Builder.byCommasWithQuotedTermsAndBackslashEscape();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is(", "));
//    assertThat(separatedString.getWrapBefore(), is("\""));
//    assertThat(separatedString.getWrapAfter(), is("\""));
//    assertThat(separatedString.getEscapeChar(), is("\\"));
//  }
//
//  @Test
//  public void testByCommasWithQuotedTermsAndDoubleBackslashEscape() {
//    Builder builder = Builder.byCommasWithQuotedTermsAndDoubleBackslashEscape();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is(","));
//    assertThat(separatedString.getWrapBefore(), is("\""));
//    assertThat(separatedString.getWrapAfter(), is("\""));
//    assertThat(separatedString.getEscapeChar(), is("\\\\"));
//  }
//
//  @Test
//  public void testByTabs() {
//    Builder builder = Builder.byTabs();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is("\t"));
//    assertThat(separatedString.getWrapBefore(), is(""));
//    assertThat(separatedString.getWrapAfter(), is(""));
//    assertThat(separatedString.getEscapeChar(), is(""));
//  }
//
//  @Test
//  public void testByLines() {
//    Builder builder = Builder.byLines();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is("\n"));
//    assertThat(separatedString.getWrapBefore(), is(""));
//    assertThat(separatedString.getWrapAfter(), is(""));
//    assertThat(separatedString.getEscapeChar(), is(""));
//  }
//
//  @Test
//  public void testSpaceSeparated() {
//    Builder builder = Builder.spaceSeparated();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is(" "));
//    assertThat(separatedString.getWrapBefore(), is(""));
//    assertThat(separatedString.getWrapAfter(), is(""));
//    assertThat(separatedString.getEscapeChar(), is(""));
//  }
//
//  @Test
//  public void testCommaSeparated() {
//    Builder builder = Builder.commaSeparated();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is(","));
//    assertThat(separatedString.getWrapBefore(), is(""));
//    assertThat(separatedString.getWrapAfter(), is(""));
//    assertThat(separatedString.getEscapeChar(), is(""));
//  }
//
//  @Test
//  public void testTabSeparated() {
//    Builder builder = Builder.tabSeparated();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is("\t"));
//    assertThat(separatedString.getWrapBefore(), is(""));
//    assertThat(separatedString.getWrapAfter(), is(""));
//    assertThat(separatedString.getEscapeChar(), is(""));
//  }
//
//  @Test
//  public void testLineSeparated() {
//    Builder builder = Builder.lineSeparated();
//    SeparatedString separatedString = builder.getSeparatedString();
//    assertThat(separatedString.getSeparator(), is("\n"));
//    assertThat(separatedString.getWrapBefore(), is(""));
//    assertThat(separatedString.getWrapAfter(), is(""));
//    assertThat(separatedString.getEscapeChar(), is(""));
//  }
}
