/*
 * This file is part of Bifrost.
 *
 * Copyright (c) 2011-2012, CraftFire <http://www.craftfire.com/>
 * Bifrost is licensed under the GNU Lesser General Public License.
 *
 * Bifrost is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bifrost is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftfire.bifrost.exceptions;

/**
 * This exception should be thrown when the library user uses a script which is not supported by Bifrost.
 */
@SuppressWarnings("serial")
public class UnsupportedScript extends Exception {
    private String message;

    /**
     * Default constructor with a default message.
     */
    public UnsupportedScript() {
        this.message = "This script is not supported by Bifrost.";
    }

    /**
     * Constructs the exception with the specified <code>message</code>.
     *
     * @param message  the message for the exception
     */
    public UnsupportedScript(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Returns the error message for the exception.
     *
     * @return error message
     */
    public String getError() {
        return this.message;
    }
}
