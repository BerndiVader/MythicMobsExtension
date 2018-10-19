package com.gmail.berndivader.mythicmobsext.mechanics.guardianbeam;

/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 Jaxon A Brown
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *  OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * The function of this class is a bit shaky. It is mostly to avoid use of NMS code to create a proper EID.
 * This class creates Entity IDs that are used by minecraft to identify entities in the protocol.
 * There are two ways to break the function of this class and risk server errors or client crashing:
 *  - A world with 2 billion entites at once might cause client crashes.
 *  - A user of this library creating 147,483,647 entities with this class would beak something. I think the server would
 *      crash, but I'm not 100% sure. Java would wrap the entity ids back to negative max value, which is likely to cause trouble.
 * @author Jaxon A Brown
 */
class EIDGen {
    private static int lastIssuedEID = 2000000000;//2 billion
    static int generateEID() {
        return lastIssuedEID++;
    }
}
