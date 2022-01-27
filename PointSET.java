
/******************************************************************************
 *  Compilation:  javac-algs4 PointSET.java
 *  Execution:    java-algs4 PointSET  <  input10.txt
 *  Dependencies: 
******************************************************************************/
import java.util.LinkedList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;

public class PointSET {
	
	  private SET<Point2D> set;
	  private int size;
	  private LinkedList<Point2D> stack;
	   public         PointSET()                               // construct an empty set of points
	   {
		   set = new SET<Point2D>();
		   size = 0;
		   this.stack = new LinkedList<>();
	   }
	   public           boolean isEmpty()                      // is the set empty?
	   {
		   return set.isEmpty();
	   }
	   public               int size()                         // number of points in the set
	   {
		   return size;
	   }
	   
	   public  void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	   {
		   if(p == null) throw new IllegalArgumentException(); 
		   if(!set.contains(p)) {
			   set.add(p);
			   this.size++;
		   }		   
	   }
	   public           boolean contains(Point2D p)            // does the set contain point p?
	   {
		   return set.contains(p);
	   }
	   public    void draw()                         // draw all points to standard draw
	   { 
		   if(this.set == null) throw new IllegalArgumentException(); 
		   for (Point2D point2d : set) {
			   StdDraw.setPenColor(StdDraw.BLACK)  ;
			   StdDraw.setPenRadius(0.01);
			point2d.draw();
		   }
		   RectHV rect = new RectHV(0.4,0.3,0.8,0.6);
		   rect.draw();
	   }
	   
	   public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
	   {
		    if(rect == null) throw new IllegalArgumentException(); 
			for (Point2D point2d : set) {
				if(rect.contains(point2d)) {
					stack.addFirst(point2d);
				}
			}   
			return stack;
	   }
	   public    Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
	   {
		  if(p == null) throw new IllegalArgumentException(); 
		  double distance = Double.MAX_VALUE;
		  Point2D champion = null;
		   for (Point2D point2d : set) {
			   if(p.compareTo(point2d) !=0) {
				   if(p.distanceSquaredTo(point2d) < distance) {
					   champion = point2d;
					   distance = p.distanceTo(point2d);
				   }   
			   }
			   
		   }
		   return champion;
	   }
	   
	   
	public static void main(String[] args) {
		
		SET<Point2D> set = new SET<Point2D>();
		PointSET ps = new PointSET();
		KdTree kdtree = new KdTree();
		
		while (!StdIn.isEmpty()) {
			double nb1 = StdIn.readDouble();
			double nb2 = StdIn.readDouble();
			set.add(new Point2D(nb1, nb2));
			kdtree.insert(new Point2D(nb1, nb2));		
		}
		ps.set = set;
		ps.draw();
		System.out.println("************testing range methode in PointSET************");
		LinkedList<Point2D> stack = (LinkedList<Point2D>) ps.range(new RectHV(0.4,0.3,0.8,0.6));
		System.out.println("************testing range methode in kdTree************");
		LinkedList<Point2D> stack1 = (LinkedList<Point2D>) kdtree.range(new RectHV(0.4,0.3,0.8,0.6));
		System.out.println("testing PointSET");
		for (Point2D point2d : stack) {
			System.out.println(point2d.x()+"  "+point2d.y());
		}
		System.out.println("testing kdTree");
		for (Point2D point2d : stack1) {
			System.out.println(point2d.x()+"  "+point2d.y());
		}
		
		System.out.println();
		System.out.println("************testing nearest methode ************");
		Point2D  p = ps.nearest(new Point2D(0.81, 0.30)); 
		Point2D  p2 = kdtree.nearest(new Point2D(0.81, 0.30));
		System.out.println("the nearest point of Point2D(0.81, 0.30) is " + p.x()+" "+p.y());
		System.out.println();
		System.out.println("the nearest point of Point2D(0.81, 0.30) is " + p2.x()+" "+p2.y());
		System.out.println("************");
		System.out.println("is 0.862 0.825 in the tree  " + kdtree.contains(new Point2D(0.862 ,0.825)));
		System.out.println("is 0.062 0.125 in the tree  " + kdtree.contains(new Point2D(0.062 ,0.125)));
	}

}
