package com.locomatix.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class ObjectGridTest {

	
	@Test
	public void testObjectGrid1x1() {
		int horizontalSlices = 1;
		int verticalSlices = 1;
		int[] count = createCountGrid(horizontalSlices, verticalSlices);
		
		Grid grid = new Grid(count, horizontalSlices, verticalSlices);
		
		System.out.println("Grid " + Arrays.toString(count));
		
		assertEquals(count[0], grid.getCount(0, 0));
		
	}
	
	
	@Test
	public void testObjectGrid1x5() {
		int horizontalSlices = 5;
		int verticalSlices = 1;
		//int[] count = createCountGrid(horizontalSlices, verticalSlices);
		
		int[] response = { 1,2,3,4,5 };
		int[] count = { 1,2,3,4,5 };
		
		Grid grid = new Grid(response, horizontalSlices, verticalSlices);
		
		System.out.println("Grid " + Arrays.toString(count));
		
		assertEquals(count[0], grid.getCount(0, 0));
		
	}
	
	
	@Test
	public void testObjectGrid5x1() {
		int horizontalSlices = 1;
		int verticalSlices = 5;
		//int[] count = createCountGrid(horizontalSlices, verticalSlices);
		
		int[] response = { 1,2,3,4,5 };
		int[] count = { 5,4,3,2,1 };
		
		Grid grid = new Grid(response, horizontalSlices, verticalSlices);
		
		System.out.println("Grid " + Arrays.toString(count));
		
		assertEquals(count[0], grid.getCount(0, 0));
		
	}
	
	
	@Test
	public void testObjectGrid4x2() {
		int horizontalSlices = 4;
		int verticalSlices = 2;
		//int[] count = createCountGrid(horizontalSlices, verticalSlices);
		
		int[] response = { 1,2,3,4,5,6,7,8 };
		int[] count = { 5,6,7,8,1,2,3,4 };
		
		Grid grid = new Grid(response, horizontalSlices, verticalSlices);
		
		System.out.println("Grid " + Arrays.toString(count));
		System.out.println("ObjectGrid: " + grid.toString());
		
		assertEquals(count[0], grid.getCount(0, 0));
		assertEquals(count[1], grid.getCount(1, 0));
		assertEquals(count[2], grid.getCount(2, 0));
		assertEquals(count[3], grid.getCount(3, 0));
		
		System.out.println("count[4]: " + count[4] + " grid(1, 0): " + grid.getCount(0, 1));
		assertEquals(count[4], grid.getCount(0, 1));
		assertEquals(count[5], grid.getCount(1, 1));
		assertEquals(count[6], grid.getCount(2, 1));
		assertEquals(count[7], grid.getCount(3, 1));
		
	}
	
	
	@Test
	public void testObjectGrid3x3() {
		
		int horizontalSlices = 3;
		int verticalSlices = 3;
		//int[] count = createCountGrid(horizontalSlices, verticalSlices);
		
		int[] response = { 1,2,3,4,5,6,7,8,9 };
		int[] count = { 7,8,9,4,5,6,1,2,3 };
		
		Grid grid = new Grid(response, horizontalSlices, verticalSlices);
		
		System.out.println("Grid " + Arrays.toString(count));
		
		
		// row 1
		assertEquals(count[0], grid.getCount(0, 0));
		assertEquals(count[1], grid.getCount(1, 0));
		assertEquals(count[2], grid.getCount(2, 0));
		
		// row 2
		assertEquals(count[3], grid.getCount(0, 1));
		assertEquals(count[4], grid.getCount(1, 1));
		assertEquals(count[5], grid.getCount(2, 1));
		
		// row 3
		assertEquals(count[6], grid.getCount(0, 2));
		assertEquals(count[7], grid.getCount(1, 2));
		assertEquals(count[8], grid.getCount(2, 2));
		
	}
	
	@Test
	public void testObjectGrid5x2() {
		int horizontalSlices = 5;
		int verticalSlices = 2;
		
		int[] response = { 1,2,3,4,5,6,7,8,9,10 };
		int[] count = { 6,7,8,9,10,1,2,3,4,5 };
		
		Grid grid = new Grid(response, horizontalSlices, verticalSlices);
		
		System.out.println("Grid " + Arrays.toString(count));
		
		assertEquals(count[0], grid.getCount(0, 0));
		assertEquals(count[1], grid.getCount(1, 0));
		assertEquals(count[2], grid.getCount(2, 0));
		assertEquals(count[3], grid.getCount(3, 0));
		assertEquals(count[4], grid.getCount(4, 0));
		
		assertEquals(count[5], grid.getCount(0, 1));
		assertEquals(count[6], grid.getCount(1, 1));
		assertEquals(count[7], grid.getCount(2, 1));
		assertEquals(count[8], grid.getCount(3, 1));
		assertEquals(count[9], grid.getCount(4, 1));
		
	}
	
	
	private int[] createCountGrid(int h, int v) {
		Random r = new Random(System.currentTimeMillis());
		int[] g = new int[h * v];
		for(int i=0; i<g.length; ++i) {
			g[i] = r.nextInt(100);
		}
		
		return g;
	}
	
}
