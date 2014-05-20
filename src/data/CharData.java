package data;

/**
 * Spacing data for the characters in the arial font PNG. Note this will likely only work for that image, and it will only work for images of the same size, assuming the sizing is sane.
 * @author Chris Friend
 *
 */
public enum CharData {

	A((byte) 37, (byte) 40, (byte) 0), B((byte) 29, (byte) 40, (byte) 0), C((byte) 34, (byte) 42, (byte) 0), D((byte) 32, (byte) 40, (byte) 0),
	E((byte) 29, (byte) 40, (byte) 0), F((byte) 26, (byte) 40, (byte) 0), G((byte) 37, (byte) 42, (byte) 0), H((byte) 30, (byte) 40, (byte) 0),
	I((byte) 5, (byte) 40, (byte) 0), J((byte) 21, (byte) 41, (byte) 0), K((byte) 33, (byte) 40, (byte) 0), L((byte) 24, (byte) 40, (byte) 0),
	M((byte) 37, (byte) 40, (byte) 0), N((byte) 30, (byte) 40, (byte) 0), O((byte) 38, (byte) 42, (byte) 0), P((byte) 29, (byte) 40, (byte) 0),
	Q((byte) 38, (byte) 45, (byte) 0), R((byte) 33, (byte) 40, (byte) 0), S((byte) 31, (byte) 42, (byte) 0), T((byte) 31, (byte) 40, (byte) 0),
	U((byte) 30, (byte) 41, (byte) 0), V((byte) 37, (byte) 40, (byte) 0), W((byte) 55, (byte) 40, (byte) 0), X((byte) 37, (byte) 40, (byte) 0),
	Y((byte) 35, (byte) 40, (byte) 0), Z((byte) 31, (byte) 40, (byte) 0),
	a((byte) 27, (byte) 30, (byte) 0), b((byte) 25, (byte) 40, (byte) 0), c((byte) 26, (byte) 30, (byte) 0), d((byte) 25, (byte) 40, (byte) 0),
	e((byte) 27, (byte) 30, (byte) 0), f((byte) 17, (byte) 40, (byte) 0), g((byte) 24, (byte) 41, (byte) 0), h((byte) 23, (byte) 40, (byte) 0),
	i((byte) 5, (byte) 40, (byte) 0), j((byte) 12, (byte) 51, (byte) 0), k((byte) 24, (byte) 40, (byte) 0), l((byte) 5, (byte) 40, (byte) 0),
	m((byte) 39, (byte) 30, (byte) 0), n((byte) 23, (byte) 30, (byte) 0), o((byte) 27, (byte) 30, (byte) 0), p((byte) 25, (byte) 41, (byte) 0),
	q((byte) 25, (byte) 41, (byte) 0), r((byte) 16, (byte) 30, (byte) 0), s((byte) 24, (byte) 30, (byte) 0), t((byte) 15, (byte) 39, (byte) 0),
	u((byte) 23, (byte) 29, (byte) 0), v((byte) 27, (byte) 29, (byte) 0), w((byte) 41, (byte) 29, (byte) 0), x((byte) 28, (byte) 29, (byte) 0),
	y((byte) 25, (byte) 40, (byte) 0), z((byte) 25, (byte) 29, (byte) 0),
	
	ZERO((byte) 25, (byte) 39, (byte) 0), ONE((byte) 15, (byte) 39, (byte) 0), TWO((byte) 26, (byte) 39, (byte) 0), THREE((byte) 25, (byte) 39, (byte) 0),
	FOUR((byte) 27, (byte) 39, (byte) 0), FIVE((byte) 25, (byte) 39, (byte) 0), SIX((byte) 26, (byte) 39, (byte) 0), SEVEN((byte) 26, (byte) 39, (byte) 0),
	EIGHT((byte) 25, (byte) 39, (byte) 0), NINE((byte) 25, (byte) 39, (byte) 0),
	
	space((byte) 32, (byte) 32, (byte) 0),
	;
	
	@SuppressWarnings("unused")
	private final byte xDim, yDim, padding;
	
	private CharData(final byte xDim, final byte yDim, final byte padding){
		this.xDim = xDim;
		this.yDim = yDim;
		this.padding = padding;
	}
	
	public byte getXDimensions(){
		return xDim;
	}
	
	public byte getYDimension(){
		return yDim;
	}
	
	public byte getPadding(){
		return 2;
	}

}
