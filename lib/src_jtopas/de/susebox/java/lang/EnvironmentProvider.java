/*
 * EnvironmentProvider.java: A central storage for Environment instances.
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
import java.util.Hashtable;
import de.susebox.java.lang.ExtNullPointerException;


//-----------------------------------------------------------------------------
// Class EnvironmentProvider
//

/**<p>
 * The <code>EnvironmentProvider</code> is a singleton object to store and provide
 * {@link Environment} instances. It provides the possibility to register and
 * retrieve <code>Environment</code> instances on a per object or per class base.
 *</p><p>
 * Since an <code>Environment</code> is designed as a substitute for some features
 * of the JDK {@link java.lang.System} class, the retrieval of the <code>Environment</code>
 * associated with a class should be possible without additional information about 
 * the context. Class-based <code>Environment</code> objects are also associated with
 * the thread, that actually registered the instance.
 *</p>
 *
 * @author  Heiko Blau
 */
public final class EnvironmentProvider {
  
  /**
   * This method returns an {@link Environment} instance that has been registered
   * for the given object. 
   *<br>
   * If there is no specific <code>Environment</code> instance available for the 
   * object then its class name is used to find a more general <code>Environment</code> 
   * instance.
   *<br>
   * If still no <code>Environment</code> could be found a default <code>Environment</code>
   * is returned, usually a {@link DefaultEnvironment} instance.
   *<br>
   * The method will always return an <code>Environment</code> instance except
   * for runtime exceptions.
   *
   * @param   obj   the object thats environment should be retrieved
   * @return  an {@link Environment} instance for the caller
   * @see     Environment
   * @see     DefaultEnvironment
   */
  public static Environment getEnvironment(Object obj) {
    Environment env = null;

    // try to find an environment going up the class hierarchy
    if (obj != null && _environmentMap != null) {
      synchronized(_syncMonitor) {
        Object iterObj = obj;
          
        do {
          if (iterObj instanceof Class) {
            env     = (Environment)_environmentMap.get(new EnvironmentKey((Class)iterObj));
            iterObj = ((Class)iterObj).getSuperclass();
          } else {
            env = (Environment)_environmentMap.get(iterObj);
            iterObj = iterObj.getClass();
          }
          if (env != null) {
            break;
          }
        } while (iterObj instanceof Class);
      }
    }
    
    // not found ? Than take the default environment
    if (env == null) {
      synchronized(_syncMonitor) {
        if (_defaultEnvironment == null) {
          _defaultEnvironment = new DefaultEnvironment();
        }
        env = _defaultEnvironment;
      }
    }
    return env;
  }
  
  /**
   * Registering an {@link Environment} for the given object. If this object
   * is a {@link java.lang.Class} instance, the <code>Environment</code> is 
   * common for all instances of this class and its subclasses.
   *
   * @param obj   the object the given {@link Environment} is for
   * @param env   the {@link Environment} to store
   * @throws NullPointerException if one of the parameters is <code>null</code>
   */
  public static void setEnvironment(Object obj, Environment env) throws NullPointerException {
    // test parameters
    if (obj == null) {
      throw new ExtNullPointerException("No object given.");
    } else if (env == null) {
      throw new ExtNullPointerException("No environment given.");
    }
    
    // create hashtable for the environments
    synchronized(_syncMonitor) {
      if (_environmentMap == null) {
        _environmentMap = new Hashtable();
      }
    
      // store the environment
      if (obj instanceof Class) {
        _environmentMap.put(new EnvironmentKey((Class)obj), env);
      } else {
        _environmentMap.put(obj, env);
      }
    }
  }
  
  
  /**
   * Removing a registered {@link Environment}. If the given object is not known
   * to the <code>EnvironmentProvider</code> the method does nothing.
   *
   * @param obj   the object thats {@link Environment} should be removed
   */
  public static void removeEnvironment(Object obj) {
    if (obj != null && _environmentMap != null) {
      if (obj instanceof Class) {
        _environmentMap.remove(new EnvironmentKey((Class)obj));
      } else {
        _environmentMap.remove(obj);
      }
    }
  }
  
  
  //---------------------------------------------------------------------------
  // inner class
  //
  
  /**
   * This class stores a {@link java.lang.Class} object and the current thread
   * to form a key for class-based environment registration
   *
   * @see EnvironmentProvider#setEnvironment
   * @see EnvironmentProvider#getEnvironment
   */
  static final class EnvironmentKey {
    
    /**
     * The constructor takes the {@link java.lang.Class} object that forms the
     * first part of the key. It automatically adds the calling thread to the 
     * key
     *
     * @param cl  the <code>Class</code> object that is the first part of the key.
     */
    public EnvironmentKey(Class cl) {
      synchronized(this) {
        _class  = cl;
        _thread = Thread.currentThread();
      }
    }
    
    /**
     * Checking the equality of this instance to another {@link java.lang.Object}. 
     *
     * @param   obj   the reference object with which to compare
     * @return  <code>true</code> if this object is the same as the obj argument; 
     *          <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      } else if (obj == null) {
        return false;
      } else if ( ! (obj instanceof EnvironmentKey)) {
        return false;
      } else {
        EnvironmentKey key = (EnvironmentKey)obj;
        
        if (_thread == key._thread && _class.equals(key._class)) {
          return true;
        } else {
          return false;
        }
      }
    }
    
    /**
     * Providing the has code for this key.
     *
     * @return  the hash code for this instance
     */
    public int hashCode() {
      return (_thread.hashCode() << 4) + _class.getName().hashCode();
    }

    //-------------------------------------------------------------------------
    // members
    //
    private Class   _class = null;
    private Thread  _thread = null;
  }
  
  
  //---------------------------------------------------------------------------
  // members
  //
  private static DefaultEnvironment _defaultEnvironment = null;
  private static Hashtable          _environmentMap     = null;
  private static Object             _syncMonitor        = new Object();
}

