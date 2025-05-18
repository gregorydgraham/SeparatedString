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
package nz.co.notgregs.example;


import nz.co.gregs.separatedstring.Builder;
import nz.co.gregs.separatedstring.Decoder;
import nz.co.gregs.separatedstring.Encoder;
import nz.co.gregs.separatedstring.SeparatedString;
import nz.co.gregs.separatedstring.SeparatedStringBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

/**
 *
 * @author gregorygraham
 */
public class IntegrationTesting {
  
  public IntegrationTesting(){
    
  }
  
  @Test
  public void firstTest(){
    final SeparatedString sepString = SeparatedStringBuilder
            .byTabs()
            .withThisBeforeEachTerm("~\"")
            .withThisAfterEachTerm("\"~")
            .withEscapeChar("==")
            .withPrefix("START")
            .withSuffix("END");
// 1000,117090058,117970084,"170,9 + 58","179,7 + 84","Flensburg Weiche, W 203 - Flensburg Grenze",Flensburg-Weiche - Flensb. Gr
    sepString.addAll("1000", "117090058~\"", "117970084", "170,9 + 58", "179,7 + 84", "Flensburg Weiche, W 203 - Flensburg Grenze", "Flensburg-Weiche - Flensb. Gr", "Albert \"The Pain\" Hallsburg");
    final String encoded = sepString.encode();
    assertThat(encoded, is("START~\"1000\"~\t~\"117090058==~\"\"~\t~\"117970084\"~\t~\"170,9 + 58\"~\t~\"179,7 + 84\"~\t~\"Flensburg Weiche, W 203 - Flensburg Grenze\"~\t~\"Flensburg-Weiche - Flensb. Gr\"~\t~\"Albert \"The Pain\" Hallsburg\"~END"));

    String[] parsed = sepString.parseToArray(encoded);
    
    assertThat(parsed.length, is(8));
    assertThat(parsed[0], is("1000"));
    assertThat(parsed[1], is("117090058~\""));
    assertThat(parsed[2], is("117970084"));
    assertThat(parsed[3], is("170,9 + 58"));
    assertThat(parsed[4], is("179,7 + 84"));
    assertThat(parsed[5], is("Flensburg Weiche, W 203 - Flensburg Grenze"));
    assertThat(parsed[6], is("Flensburg-Weiche - Flensb. Gr"));
    assertThat(parsed[7], is("Albert \"The Pain\" Hallsburg"));
  }
  
  @Test
  public void builderEncoderDecoderTest(){
    final Builder builder = Builder.byTabs()
            .withThisBeforeEachTerm("~\"")
            .withThisAfterEachTerm("\"~")
            .withEscapeChar("==")
            .withPrefix("START")
            .withSuffix("END");
    Encoder encoder = builder.encoder();
    Decoder decoder =builder.decoder();
// 1000,117090058,117970084,"170,9 + 58","179,7 + 84","Flensburg Weiche, W 203 - Flensburg Grenze",Flensburg-Weiche - Flensb. Gr
    encoder.addAll("1000", "117090058~\"", "117970084", "170,9 + 58", "179,7 + 84", "Flensburg Weiche, W 203 - Flensburg Grenze", "Flensburg-Weiche - Flensb. Gr", "Albert \"The Pain\" Hallsburg");
    final String encoded = encoder.encode();
    assertThat(encoded, is("START~\"1000\"~\t~\"117090058==~\"\"~\t~\"117970084\"~\t~\"170,9 + 58\"~\t~\"179,7 + 84\"~\t~\"Flensburg Weiche, W 203 - Flensburg Grenze\"~\t~\"Flensburg-Weiche - Flensb. Gr\"~\t~\"Albert \"The Pain\" Hallsburg\"~END"));

    String[] parsed = decoder.decodeToArray(encoded);

    assertThat(parsed.length, is(8));
    assertThat(parsed[0], is("1000"));
    assertThat(parsed[1], is("117090058~\""));
    assertThat(parsed[2], is("117970084"));
    assertThat(parsed[3], is("170,9 + 58"));
    assertThat(parsed[4], is("179,7 + 84"));
    assertThat(parsed[5], is("Flensburg Weiche, W 203 - Flensburg Grenze"));
    assertThat(parsed[6], is("Flensburg-Weiche - Flensb. Gr"));
    assertThat(parsed[7], is("Albert \"The Pain\" Hallsburg"));
  }
}
