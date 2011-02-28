/*
 * Environment.java: Alternative System interface.
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

//-----------------------------------------------------------------------------
// Imports
//
import java.io.InputStream;
import java.io.PrintStream;


//-----------------------------------------------------------------------------
// Interface Environment
//

/**<p>
 * A <code>Environment</code> object is a substitute for the usual environment
 * as defined in the {@link java.lang.System} class, most important the 
 * <code>stdin</code> and <code>stdout</code> channels {@link java.lang.System#in} 
 * and {@link java.lang.System#out}.
 *</p><p>
 * Environments are especially useful in classes that can be used both standalone 
 * (having a <code>main</code> method) or in an application where the class is
 * only one of many. Another scenario would be a simple class designed for use 
 * from the command line that should suddenly be invoked in GUI framework without
 * redirecting the default channels {@link java.lang.System#in} and 
 * {@link java.lang.System#out}.
 *</p><p>
 * To obtain an <code>Environment</code> instance or to store such instances 
 * use the class {@link EnvironmentProvider}:
 *<block><pre>
 *    Environment env = EnvironmentProvider.getEnvironment(this);
 *</pre></block>
 *</p>
 *
 * @author  Heiko Blau
 */
public interface Environment {
  
  /**
   * This method returns the substitution for {@link java.lang.System#in}, the
   * standard input channel. Instead of a public member like in the <code>System</code>
   * class, we prefer the method version since it is more flexible.
   *
   * @return  the {@link java.io.InputStream} that serves as standard input
   * @throws  UnsupportedOperationException if there is no stdin channel available
   * @see     java.lang.System#in
   */
  public InputStream in() throws UnsupportedOperationException;
  /*-->
  {
    throw UnsupportedOperationException;
  }
  -->*/
  
  /**
   * This method returns the substitution for {@link java.lang.System#out}, the
   * standard output channel. Instead of a public member like in the <code>System</code>
   * class, we prefer the method version since it is more flexible.
   *
   * @return  the {@link java.io.PrintStream} that serves as standard output
   * @throws  UnsupportedOperationException if there is no stdout channel available
   * @see     java.lang.System#out
   */
  public PrintStream out() throws UnsupportedOperationException;
  /*-->
  {
    throw UnsupportedOperationException;
  }
  -->*/
  
  /**
   * This method returns the substitution for {@link java.lang.System#err}, the
   * standard error channel. Instead of a public member like in the <code>System</code>
   * class, we prefer the method version since it is more flexible.
   *
   * @return  the {@link java.io.PrintStream} that serves as standard error output
   * @throws  UnsupportedOperationException if there is no stderr channel available
   * @see     java.lang.System#err
   */
  public PrintStream err() throws UnsupportedOperationException;
  /*-->
  {
    throw UnsupportedOperationException;
  }
  -->*/
  
  /**
   * During different stages of program execution various exit codes can be set
   * using this method. For instance, a program may set the exit code to a positive
   * number indicating that it is still running. A negative exit code may stand
   * for errors while 0 is the usual OK code.
   *
   * @param   status  the exit code of an application
   * @see     java.lang.System#exit
   * @see     #getExitStatus
   * @see     #exit
   */
  public void setExitStatus(int status);
  /*-->
  {
    _exitStatus = status;
  }
  -->*/
  
  /**
   * Retrieving the currently set exit code.
   *
   * @return the currently set exit code of an application
   */
  public int getExitStatus();
  /*-->
  {
    return _exitStatus;
  }
  -->*/


  /**
   * This method exits the instance of its <code>Environment</code> implementation.
   * After calling <code>exit</code> the <code>Environment</code> instance is
   * generally not any longer usable. In particular, an implementation can choose
   * to call {@link java.lang.System#exit}.
   *
   * @throws  UnsupportedOperationException if the method is not available
   * @see   #setExitStatus
   * @see   java.lang.System#exit
   */
  public void exit() throws UnsupportedOperationException;
  /*-->
  {
    throw UnsupportedOperationException;
  }
  -->*/
  
  
  //---------------------------------------------------------------------------
  // members
  //
  /*-->
  private int _exitStatus = 0;
  -->*/
}
