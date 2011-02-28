/*
 * DefaultEnvironment.java: Environment implementation based on the Java System class.
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
// Class DefaultEnvironment
//

/**<p>
 * The <code>DefaultEnvironment</code> implements the {@link Environment} interface 
 * using the JDK class {@link java.lang.System}. Instances of this class are
 * returned by the singleton {@link EnvironmentProvider} ({@link EnvironmentProvider#getEnvironment})
 * if there is no other <code>Environment</code> available for the caller.
 *</p>
 *
 * @see     Environment
 * @see     EnvironmentProvider
 * @author  Heiko Blau
 */
public class DefaultEnvironment implements Environment {
  
  //---------------------------------------------------------------------------
  // methods of the Environment interface
  //

  /**
   * This method returns {@link java.lang.System#in}, the standard input channel.
   *
   * @return  the {@link java.io.InputStream} that serves as standard input
   * @see     java.lang.System#in
   */
  public InputStream in() {
    return System.in;
  }
  
  /**
   * This method returns {@link java.lang.System#out}, the standard output channel.
   *
   * @return  the {@link java.io.PrintStream} that serves as standard output
   * @see     java.lang.System#out
   */
  public PrintStream out() {
    return System.out;
  }
  
  /**
   * This method returns {@link java.lang.System#err}, the standard error channel.
   *
   * @return  the {@link java.io.PrintStream} that serves as standard error output
   * @see     java.lang.System#err
   */
  public PrintStream err() {
    return System.err;
  }

  /**
   * This method stores the exit code of an application. It can be called more
   * than once.
   *
   * @param status  the exit code of an application
   * @see   java.lang.System#exit
   */
  public void setExitStatus(int status) {
    _exitStatus = status;
  }

  /**
   * Retrieving the currently set exit code.
   *
   * @return the currently set exit code of an application
   */
  public int getExitStatus() {
    return _exitStatus;
  }

  /**
   * This method exits the instance of its <code>Environment</code> implementation.
   * After calling <code>exit</code> the <code>Environment</code> instance is
   * generally not any longer usable. In particular, an implementation can choose
   * to call {@link java.lang.System#exit}.
   *
   * @see   #setExitStatus
   * @see   java.lang.System#exit
   */
  public void exit() throws UnsupportedOperationException {
    System.exit(getExitStatus());
  }

  
  //---------------------------------------------------------------------------
  // members
  //
  private int _exitStatus = 0;
}
