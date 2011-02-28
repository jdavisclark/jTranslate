/*
 * ThrowableMessageFormatter.java: formatting a throwable message
 *
 * Copyright (C) 2001 Heiko Blau
 *
 * This file belongs to the Susebox Java Core Library (Susebox JCL).
 * The Susebox JCL is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with the Susebox JCL. If not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307
 *   USA
 *
 * or check the Internet: http://www.fsf.org
 *
 * Contact:
 *   email: heiko@susebox.de
 */

package de.susebox.java.lang;

//------------------------------------------------------------------------------
// Imports
//
import java.lang.reflect.Method;
import java.text.MessageFormat;
import de.susebox.java.lang.ThrowableList;


//------------------------------------------------------------------------------
// Class ThrowableMessageFormatter
//

/**<p>
 * This class is used by the implementations of the {@link ThrowableList} interface.
 * Its method {@link #getMessage} formats the message that should be returned by 
 * the {@link java.lang.Throwable#getMessage} overridden by implementations of the 
 * {@link ThrowableList} interface.
 *</p>
 *
 * @see     ThrowableList
 * @see     java.lang.Throwable
 * @see     java.text.MessageFormat
 * @author 	Heiko Blau
 */
public final class ThrowableMessageFormatter {
  
  /**
   * Message indentation for nested exceptions.
   */
  public static final String MSG_IDENTATION = "    ";
  
  /**
   * This method should be called by all implementations of the {@link ThrowableList}
   * interface in their {@link java.lang.Throwable#getMessage} implementation. It
   * ensures that the formatting of throwable lists, nested or wrapped exceptions 
   * is done in a consistent way.
   *<br>
   * The method returns an throwable message assembled in this way:
   *<ol><li>
   * If the calling throwable is a wrapper throwable (see {@link ThrowableList#isWrapper}),
   * it delegates the call to the wrapped throwable (<code>wrappedEx.getMessage()</code>).
   *</li><li>
   * If there is a nested throwable (the calling throwable has a message of its own),
   * the returned message starts with the string returned by the {@link java.lang.Object#toString}
   * method, followed and separated by a end-of-line sequence by the formatted message
   * of the calling throwable.
   *</li><li>
   * If the calling throwable has only a message without parameters, this message
   * is either appended to the string produced by processing a nested throwable or
   * is taken as the entire return value if there is no nested or subsequent throwable
   * present.
   *</li><li>
   * If the calling throwable provides parameters along with a format string (the
   * return value of <code>super.getMessage</code> is interpreted as a format string),
   * a formatted message produced by {@link java.text.MessageFormat#format} is either
   * appended to the string produced by processing a nested throwable or is taken as
   * the entire return value if there is no nested or subsequent throwable present.
   *</li></ol>
   *
   * @param   ex  the calling throwable
   * @return  the formatted throwable message
   * @see     java.text.MessageFormat
   * @see     ThrowableList
   */
  public static final String getMessage(ThrowableList ex) {
    // wrapped exceptions return theri own message
    if (ex.isWrapper()) {
      return ex.getCause().getMessage();
    }
    
    // prepare the formatting
    StringBuffer  msg  = new StringBuffer();
    String        fmt  = ex.getFormat();
    Throwable     next = ex.getCause();
    
    // message of the nested or next throwable first
    if (next != null) {
      msg.append(EOL_SEQUENCE);
      msg.append(MSG_IDENTATION);
      msg.append(next.toString());
      if (fmt != null) {
        msg.append(EOL_SEQUENCE);
        msg.append(MSG_IDENTATION);
      }
    }

    // and now our own message
    if (fmt != null) {
      Object[] args = ex.getArguments();
      
      if (args == null) {
        msg.append(fmt);
      } else {
        try {
          msg.append(MessageFormat.format(fmt, args));
        } catch (IllegalArgumentException argEx) {
          msg.append(argEx.getMessage());
          msg.append(EOL_SEQUENCE);
          msg.append("While formatting this message:");
          msg.append(EOL_SEQUENCE);
          msg.append(fmt);
        }
      }
    }
    return msg.toString();
  }

  //---------------------------------------------------------------------------
  // class constructor
  //
  static {
    try {
      Method causeMethod = Throwable.class.getMethod("getCause", new Class[] {} );
      GET_CAUSE_AVAILABLE = true;
    } catch (NoSuchMethodException ex) {
      GET_CAUSE_AVAILABLE = false;
    }
  }
  
  //---------------------------------------------------------------------------
  // members
  //
  private static final  String  EOL_SEQUENCE        = System.getProperty("line.separator");
  private static        boolean GET_CAUSE_AVAILABLE;
}
