package edu.fudan.sa.circle.xjade.invocation;

import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * An object input stream which loads its classes from the provided context.
 *
 * @author Jens Reimann
 *          
 */
public class BundleObjectInputStream extends ObjectInputStream {
	private final BundleContext context;

	public BundleObjectInputStream(final InputStream in, final BundleContext context) throws IOException {
		super(in);
		this.context = context;
	}

	@Override
	protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		try {
			String name = desc.getName();
			System.out.println(name);
			Class<?> clazz = this.context.getBundle().loadClass(name);
			System.out.println(clazz.getClassLoader());
			return clazz;
		} catch (Exception e) {
			System.out.println(e.getClass());
		}
		return super.resolveClass(desc);
	}
}