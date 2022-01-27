
import java.util.LinkedList;

import java.util.Stack;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
/******************************************************************************
 *  Compilation:  javac-algs4 KdTree.java
 *  Execution:    java-algs4 KdTree  <  input1.txt
 *  Dependencies: 
******************************************************************************/
public class KdTree {

	private Node root;
	private Stack<Node> s ;
	private LinkedList<Point2D> stack;
	private Point2D champion;
	private double bestdistance ;
	private int size;
	
	// construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
    }
	public void insert(Point2D p)
	{ /* see next slides */
		//int level = 0;
		if(!contains(p))	root = insert(root,null,p);		
	}
	private Node insert(Node x, Node parent,Point2D p) {
		if(this.root == null ) {
			RectHV rect = new RectHV(0,0,1,1);						
			RectHV separate = new RectHV(p.x(),0,p.x(),1);
			Node n = new Node(p, true,rect,separate);
			size++;
			return n;
			//return new Node(p, null,true,new RectHV(0,0,1,1));
		}
		
		if (x == null) {
			
			if(parent.xabscisse == true) {
				if(p.x() < parent.p.x()) {
					RectHV rect = new RectHV(parent.rect.xmin(),parent.rect.ymin(),parent.p.x(),parent.rect.ymax());																				
					RectHV separate = new RectHV(parent.rect.xmin(),p.y(),parent.p.x(),p.y());
					Node n = new Node(p, !parent.xabscisse, rect,separate);		
					size++;
					return n ;
				}else {
					RectHV rect = new RectHV(parent.p.x(),parent.rect.ymin(),parent.rect.xmax(),parent.rect.ymax());															
					RectHV separate = new RectHV(parent.p.x(),p.y(),parent.rect.xmax(),p.y());
					Node n = new Node(p, !parent.xabscisse, rect,separate);	
					size++;
					return n ;
				}
			}else {
				if(p.y() < parent.p.y()) {
					//rectangle bottom
					RectHV rect = new RectHV(parent.rect.xmin(),parent.rect.ymin(),parent.rect.xmax(),parent.p.y());										
					RectHV separate = new RectHV(p.x(),parent.rect.ymin(),p.x(),parent.p.y());
					Node n = new Node(p,!parent.xabscisse, rect,separate);
					size++;
					return n ;
				}else {
					RectHV rect = new RectHV(parent.rect.xmin(),parent.p.y(),parent.rect.xmax(),parent.rect.ymax());											
					
					/*StdDraw.setPenColor(StdDraw.BLACK)  ;
					StdDraw.setPenRadius(0.03);
					p.draw();
					StdDraw.setPenColor(StdDraw.RED)  ;
					StdDraw.setPenRadius(0.01);*/
					RectHV separate = new RectHV(p.x(),parent.p.y(),p.x(),parent.rect.ymax());
					Node n = new Node(p,!parent.xabscisse, rect,separate);	
					size++;
					return n ;	
				}
			}
		}
		//if the current node has x abscisse as comparable key  
		if(x.xabscisse == true) {
			if( p.x() < x.p.x()) {
				//go left
				x.lb = insert(x.lb ,x ,p/*,level++*/);
			}else {
				//go right
				x.rt = insert(x.rt ,x ,p/*,level++*/);
			}
		}else {
			if( p.y() < x.p.y()) {
				//go bottom  
				x.lb = insert(x.lb ,x ,p/*,level++*/);
			}else {
				//go up
				x.rt = insert(x.rt ,x ,p/*,level++*/);
			}
		}
		return x;		
	}
	
	private Iterable<Node> iterator(){
		this.s = new Stack<Node>();
		inorder(root, s);
		return s;
	}
	
	private void inorder(Node root2, Stack<Node> s) {
		if (root2 == null) return;
		inorder(root2.lb,  s);
		s.add(root2);		
		inorder(root2.rt,  s);
	}

	public    void draw() {
		Stack<Node> s  = (Stack<Node>) iterator();
		//throw new IllegalArgumentException("the stack is empty"); 
		if(s.isEmpty()) throw new IllegalArgumentException("the stack is empty");
		/*for (int i=0 ; i<s.size();i++) {
			
			System.out.println(s.get(i).p.x()+"   "+s.get(i).p.y());
		}
		System.out.println(s.size());*/
			while(!s.isEmpty()) {
				Node no = s.pop();
				//draw the point			
				StdDraw.setPenColor(StdDraw.BLACK)  ;
				StdDraw.setPenRadius(0.03);
				System.out.println(no.p.x() + " "+no.p.y());
				no.p.draw();
				
				//draw the subdivision 
				if(no.xabscisse == true) {
					StdDraw.setPenColor(StdDraw.RED)  ;
					StdDraw.setPenRadius(0.01);
					no.separate_line.draw();
				}else {
					StdDraw.setPenColor(StdDraw.BLUE)  ;
					StdDraw.setPenRadius(0.01);
					no.separate_line.draw();
				}
			}											
		
	}
	
	private static class Node {		 
		   private RectHV separate_line ;
		   private boolean xabscisse;
		   private Point2D p;      // the point
		   private RectHV rect;    // the axis-aligned rectangle corresponding to this node
		   private Node lb;        // the left/bottom subtree
		   private Node rt;        // the right/top subtree
		   
		public Node(Point2D p ,boolean xabscisse ,RectHV rect, RectHV separate_line ) {			
			this.xabscisse = xabscisse;
			this.p = p;
			this.rect = rect;
			this.separate_line = separate_line;
		}
				   
	}
	
   public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle (or on the boundary)
   {
	    if(rect == null) throw new IllegalArgumentException(); 
	    this.stack = new LinkedList<>();	    
	    search(root,rect ,this.stack);
	    	    
	    return stack;
   }	    
	private void search(Node root2,RectHV rect1 ,LinkedList<Point2D> stack2) {
		
		Node x = root2;
		if(x == null ) return;
		
		if(x.rect.intersects(rect1)) {
			if(rect1.contains(x.p)) {
				stack.add(x.p);
			}
			
			search(x.lb ,rect1 ,stack);
			search(x.rt ,rect1 ,stack);
		}else {
			return ; 
		}
	
	}
	
	public Point2D nearest(Point2D query) {
		Node x = root;
		champion = null;
		bestdistance = Double.MAX_VALUE;
		if(query == null || x == null) throw new IllegalArgumentException(); 
		SearchNN(x,query);
		return champion;
	}

	private void SearchNN(Node x, Point2D query) {
		if(x == null ) return;
		double orientation_difference = x.rect.distanceSquaredTo(query);//
		// if this bounding box is too far, do nothing
		if(orientation_difference > bestdistance) return ;				
		
		// if this point is better than the best:
		double best_distance_sofar = x.p.distanceSquaredTo(query);
		if(best_distance_sofar < bestdistance) {
			this.champion = x.p;
			this.bestdistance = best_distance_sofar;
		}	
		
		//System.out.println(champion.x() + " " +champion.y());
		
		if(x.xabscisse == true ) {
			if(x.p.x() > query.x()) {
				//go left
				SearchNN( x.lb, query);									
				SearchNN(x.rt, query) ;//
			}
				
			else {
				//go right				
				SearchNN(x.rt, query) ;//
													
				SearchNN(x.lb, query) ;
			 }
						
		}else {
			if(x.p.y() > query.y() ) {
				//go down				
				SearchNN(x.lb, query) ;												
				//go up
				SearchNN(x.rt, query) ;//
				
			}else {
				//go up				
				SearchNN(x.rt, query) ;									
				//go down
				SearchNN(x.lb, query) ;
				
			}
						
		}
	 //if	
	}
	public boolean contains(Point2D p) {
		 if (p == null)   throw new java.lang.IllegalArgumentException("Point2D p is null");
	     if (root == null)     	return false;
	        
		Node x = root;
		while (x != null)
		{
			if(x.xabscisse) {								
				if(p.x() < x.p.x()) x = x.lb;
				else if (p.x() > x.p.x()) x = x.rt;
				else {
					if(p.equals(x.p)) return true;
					else {
						return false;
					}
				}
			}else {
											
				if(p.y() < x.p.y()) x = x.lb;
				else if (p.y() > x.p.y()) x = x.rt;
				else {
					if(p.equals(x.p)) return true;
					else {
						return false;
					}
				}
			}
			
		}
		
		return false;
		
	}
	public boolean isEmpty() {
		return root==null;		
	}
	public int size() {
		return size;
	}
	public static void main(String[] args) {
		
		KdTree kt = new KdTree();		
		while (!StdIn.isEmpty()) {
			double nb1 = StdIn.readDouble();
			double nb2 = StdIn.readDouble();
			kt.insert(new Point2D(nb1, nb2));	
			//System.out.println(nb1+" "+ nb2 +" point is added");			
			
		}
		
		//kt.draw();
		/*StdDraw.setPenColor(StdDraw.RED)  ;
		StdDraw.setPenRadius(0.01);
		RectHV rect = new RectHV(kt.root.p.y(),0,kt.root.p.y(),1);
		rect.draw();*/
	}	
}


