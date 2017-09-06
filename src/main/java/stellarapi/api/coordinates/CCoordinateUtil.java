package stellarapi.api.coordinates;

import stellarapi.api.lib.math.Matrix4;

public class CCoordinateUtil {

	public static Matrix4 evaluate(Iterable<ICoordinateElement> elements, CoordContext context) {
		Matrix4 evaluated = new Matrix4().setIdentity();
		for(ICoordinateElement element : elements) {
			if(!context.supportContext(element.requiredContextTypes()));
				// TODO CoordSystem exception
			evaluated.preMult(element.transformMatrix(context));
		}
		return evaluated;
	}

	public static Matrix4 evaluateInverse(Iterable<ICoordinateElement> elements, CoordContext context) {
		Matrix4 evaluated = new Matrix4().setIdentity();
		for(ICoordinateElement element : elements) {
			if(!context.supportContext(element.requiredContextTypes()));
				// TODO CoordSystem exception
			evaluated.postMult(element.inverseTransformMatrix(context));
		}
		return evaluated;
	}
}
