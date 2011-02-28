/*
 * ThrowableList.java: Interface for throwable stacks
 *
 * Copyright (C) 2002 Heiko Blau
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

/*-->
import de.susebox.java.lang.ThrowableList;
import de.susebox.java.lang.ThrowableMessageFormatter;
-->*/


//------------------------------------------------------------------------------
// Interface ThrowableList
//

/**<p>
 * This interface should be implemented by classes derived from {@link java.lang.Throwable}
 * that may contain a stacked, additional or wrapped throwable.
 *</p><p>
 * Such cases are common when
 *<ul><li>
 *   a method implements a certain interface method that allows for a specific
 *   throwable like IOException, but the method itself may encounter a different
 *   throwable type like SQLException (wrapped throwable)
 *</li><li>
 *   one application layer catches an throwable only to add its specific
 *   information in form of another throwable (throwable stack, nested throwable
 *   like in SQLException or MessagingException).
 *</li></ul>
 *</p><p>
 * We provide the expected code in block comments starting with --&gt;, terminated
 * by --&gt;. Note that the provided code also includes a new implementation of
 * the base class method {@link java.lang.Throwable#getMessage} using the text
 * formatting capabilities of {@link java.text.MessageFormat}.
 *<p></p>
 * <strong>Note:</strong> This interface replaces the formerly used <code>ExceptionList</code>
 * interface.
 *<p></p>
 * <strong>Note:</strong> With JDK 1.4, the <i>chained exception facility</i> 
 * is available, that implements a Throwable stack (or list of causes) already
 * in {@link java.lang.Throwable}. Still, the notion of a wrapped exception and
 * the delayed formatting is a reason for this interface.
 *</p>
 *
 * @version	1.00, 2001/06/26
 * @author 	Heiko Blau
 */
public interface ThrowableList {
  
  /**
   * Retrieving the cause of a <code>Throwable</code>. This is the method introduced
   * with JDK 1.4. It replaces the older {@link #nextThrowable}.
   *
   * @return the cause of this <code>Throwable</code>
   * @see java.lang.Throwable#getCause
   */
  public Throwable getCause();
  
  /**
   * Method to traverse the throwable list. By convention, <code>nextThrowable</code>>
   * returns the "earlier" throwable. By walking down the throwable list one gets the
   * the following meaning:
   *<br>
   * this happened because nextThrowable happened because nextThrowable happened...
   *<br>
   * The next throwable has usually one of the following meaning:
   *<br><ul><li>
   *  It is the "real" throwable. An interface implementation might be allowed to 
   *  throw only <code>IOException</code>, but actually has to pass on a 
   *  <code>SQLException</code>. That could be done by wrapping the <code>SQLException</code>
   *  into the <code>IOException</code>.
   *</li><li>
   *  The next throwable is "deeper" cause of this one (often called a nested
   *  throwable). A file couldn't be read in the first place and therefore not be 
   *  attached to a mail. Both this throwable and the one nested inside have their
   *  own message.
   *</li></li>
   *  There are more than one basic throwable to be propagated. A simple parser 
   *  might return all syntax errors in one throwable list.
   *</li></ul>
   *
   * @return the "earlier" throwable
   * @deprecated use the JDK 1.4 call {@link #getCause} instead
   */
  public Throwable nextThrowable();
  /*-->
  {
    return _next;
  }
  -->*/
  
  
  /**
   * Check if <code>this</code> is only a throwable that wraps the real one. This
   * might be nessecary to pass an throwable incompatible to a method declaration.
   *
   * @return <code>true</code> if this is a wrapper throwable,
   *         <code>false</code> otherwise
   */
  public boolean isWrapper();
  /*-->
  {
    return _isWrapper;
  }
  -->*/
  
  /**
   * Getting the format string of a throwable message. This can also be the 
   * message itself if there are no arguments.
   *
   * @return  the format string being used by {@link java.text.MessageFormat}
   * @see     #getArguments
   */
  public String getFormat();
  /*-->
  {
    return super.getMessage();
  }
  -->*/
  
  /**
   * Retrieving the arguments for message formats. These arguments are used by
   * the {@link java.text.MessageFormat#format} call.
   *
   * @return  the arguments for a message format
   * @see     #getFormat
   */
  public Object[] getArguments();
  /*-->
  {
    return _args;
  }
  -->*/
  
  
  //---------------------------------------------------------------------------
  // implementation code templates
  //
  
  /**
   * This constructor takes a simple message string like ordinary Java 
   * {@link java.lang.Throwable} classes. This is the most convenient form to 
   * construct an <code>ThrowableList</code> throwable.
   *
   * @param msg   message for this <code>Throwable</code> instance
   */
  /*-->
  public <<WHICH>>Throwable(String msg) {
    this(null, msg, null);
  }
  -->*/
  
  /**
   * This constructor should be used for wrapping another {@link java.lang.Throwable}. 
   * While reading data an <code>IOException</code> may occur, but a certain interface 
   * requires a <code>SQLException</code>. Simply use:
   *<blockquote><pre>
   * try {
   *   ...
   * } catch (NullPointerException ex) {
   *   throw new ExtNoSuchMethodException(ex);
   * }
   *</pre></blockquote>
   *
   * @param trowable  the <code>Throwable</code> to wrap
   */
  /*-->
  public <<WHICH>>Throwable(Throwable throwable) {
    this(throwable, null, null);
  }
  -->*/
  
  /**
   * If one likes to add ones own information to an throwable, this constructor is
   * the easiest way to do so. By using such an approach a throwable trace with useful
   * additional informations (which file could be found, what username is unknown)
   * can be realized:
   *<blockquote><pre>
   * try {
   *   ...
   * } catch (SQLException ex) {
   *   throw new MyException(ex, "while connecting to " + url);
   * }
   *</pre></blockquote>
   *
   * @param throwable   the inner throwable
   * @param msg         throwable message
   */
  /*-->
  public <<WHICH>>Throwable(Throwable throwable, String msg) {
    this(throwable, msg, null);
  }
  -->*/
  
  /**
   * This constructor takes a format string and its arguments. The format string
   * must have a form that can be used by {@link java.text.MessageFormat} methods.
   * That means:
   *<blockquote><pre>
   *    java.text.Message.format(fmt, args)
   *</pre></blockquote>
   * is similar to
   *<blockquote><pre>
   *    new MyException(fmt, args).getMessage();
   *</pre></blockquote>
   *
   * @param fmt   throwable message format
   * @param args  arguments for the given format string
   */
  /*-->
  public <<WHICH>>Throwable(String fmt, Object[] args) {
    this(null, msg, args);
  }
  -->*/
  
  /**
   * This is the most complex way to construct an <code>ThrowableList</code>-
   * Throwable.<br>
   * An inner throwable is accompanied by a format string and its arguments.
   * Use this constructor in language-sensitive contexts or for formalized messages.
   * The meaning of the parameters is explained in the other constructors.
   *
   * @param throwable   the inner throwable
   * @param fmt         throwable message
   * @param args        arguments for the given format string
   */
  /*-->
  public <<WHICH>>Throwable(Throwable throwable, String fmt, Object[] args) {
    super(fmt);
   
    if (throwable != null && fmt == null) {
      _isWrapper = true;
    } else {
      _isWrapper = false;
    }
    _next = throwable;
    _args = args;
  }
  -->
   
  /**
   * Implementation of the standard {@link java.lang.Throwable#getMessage} method to
   * meet the requirements of formats and format arguments as well as wrapper
   * exceptions.<br>
   * If this is a wrapper throwable then the <code>getMessage</code> of the wrapped
   * throwable is returned.<br>
   * If this is not a wrapper throwable: if no arguments were given in the 
   * constructor then the format parameter is taken as the formatted message itself. 
   * Otherwise it is treated like the patter for the {@link java.text.MessageFormat#format}
   * method.
   *
   * @return  the formatted throwable message
   * @see     java.text.MessageFormat
   */
  /*-->
  public String getMessage() {
    return ThrowableMessageFormatter.getMessage(this);
  }
  -->*/
  
  
  //---------------------------------------------------------------------------
  // members
  //
  /**
   * the parameters to be used when formatting the throwable message
   */
  /*-->
  protected Object[]  _args       = null;
  -->*/
  
  /**
   * The wrapped, nested of next throwable.
   */
  /*-->
  protected Throwable _next       = null;
  -->*/

  /**
   * If <code>true</code> this is only a wrapper throwable with the real one
   * being returned by {@link #nextThrowable}, <code>false</code> for standalone, 
   * nested or subsequent exceptions
   */
  /*-->
  protected boolean   _isWrapper  = false;
  -->*/
}
