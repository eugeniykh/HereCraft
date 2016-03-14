package Vector;


public class Vector2i
{
	private int x;
	private int y;

	public Vector2i()
	{
		this(0, 0);
	}
	public Vector2i(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public float dot(Vector2i r)
	{
		return x * r.getX() + y * r.getY();
	}
	
	public Vector2i add(Vector2i r)
	{
		return new Vector2i(x + r.getX(), y + r.getY());
	}
	
	public Vector2i add(int r)
	{
		return new Vector2i(x + r, y + r);
	}
	
	public Vector2i substract(Vector2i r)
	{
		return new Vector2i(x - r.getX(), y - r.getY());
	}
	
	public Vector2i substract(int r)
	{
		return new Vector2i(x - r, y - r);
	}
	
	public Vector2i multiply(Vector2i r)
	{
		return new Vector2i(x * r.getX(), y * r.getY());
	}
	
	public Vector2i multiply(int r)
	{
		return new Vector2i(x * r, y * r);
	}
	
	public Vector2i divide(Vector2i r)
	{
		return new Vector2i(x / r.getX(), y / r.getY());
	}
	
	public Vector2i divide(int r)
	{
		return new Vector2i(x / r, y / r);
	}
	
	public Vector2i abs()
	{
		return new Vector2i(Math.abs(x), Math.abs(y));
	}
	
	public int getX()
	{
		return x;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
}
