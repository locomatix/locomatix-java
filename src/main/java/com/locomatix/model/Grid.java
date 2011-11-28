/**
 * Copyright 2011 Locomatix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.locomatix.model;

import java.util.Arrays;

public class Grid {
	
	private final int[] grid;
	private final int horizontalSlices;
	private final int verticalSlices;
	
	public Grid(int[] grid, int horizontalSlices, int verticalSlices) {
	  if (horizontalSlices < 1)
	    throw new IllegalArgumentException("HorizontalSlices (" + horizontalSlices + ") must be at least 1.");
	  if (verticalSlices < 1)
	    throw new IllegalArgumentException("VerticalSlices (" + verticalSlices + ") must be at least 1.");
	  this.grid = new int[grid.length];
		for(int i=0; i<verticalSlices; ++i) {
			int rowIndex = horizontalSlices * i;
			int destPos = (verticalSlices - i - 1) * horizontalSlices;
			System.arraycopy(grid, rowIndex, this.grid, destPos, horizontalSlices);
		}
		
		this.horizontalSlices = horizontalSlices;
		this.verticalSlices = verticalSlices;
	}
	
	public int getCount(int x, int y) {
		return grid[horizontalSlices * y + x];
	}
	
	public int getHorizontalSlices() {
		return horizontalSlices;
	}
	
	public int getVerticalSlices() {
		return verticalSlices;
	}
	
	public int[] getGrid() {
		return grid;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Arrays.toString(grid));
		return sb.toString();
	}
}
