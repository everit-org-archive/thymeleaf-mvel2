/**
 * This file is part of Everit - Thymeleaf MVEL2 Extension.
 *
 * Everit - Thymeleaf MVEL2 Extension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - Thymeleaf MVEL2 Extension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - Thymeleaf MVEL2 Extension.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.everit.osgi.thymeleaf.mvel2.tests;

public class UserDTO {

    private String name;

    private long age;

    public UserDTO(String name, long age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public long getAge() {
        return age;
    }

}
