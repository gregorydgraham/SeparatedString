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
    sep.addAll(2, 3, "More");
    String encoded = sep.encode();
    System.out.println(encoded);
    System.out.println("<ol>\n<li>1</li>\n<li>2</li>\n<li>3</li>\n<li>More</li>\n</ol>\n");
    assertThat(encoded, is("<ol>\n<li>1</li>\n<li>2</li>\n<li>3</li>\n<li>More</li>\n</ol>\n"));
  }

  @Test
  public void testHTMLUnorderedList() {
    SeparatedString sep = SeparatedStringBuilder.htmlUnorderedList();
    sep.add(1);
    sep.addAll(2, 3, "More");
    String encoded = sep.encode();
    System.out.println(encoded);
    System.out.println("<ul>\n<li>1</li>\n<li>2</li>\n<li>3</li>\n<li>More</li>\n</ul>\n");
    assertThat(encoded, is("<ul>\n<li>1</li>\n<li>2</li>\n<li>3</li>\n<li>More</li>\n</ul>\n"));
  }
}
