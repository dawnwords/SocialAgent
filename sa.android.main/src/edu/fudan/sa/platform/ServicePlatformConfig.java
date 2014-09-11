package edu.fudan.sa.platform;

import org.apache.felix.framework.cache.BundleCache;
import org.apache.felix.framework.util.FelixConstants;
import org.twdata.pkgscanner.ExportPackage;
import org.twdata.pkgscanner.PackageScanner;

import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Properties;

/**
 * Because it is somehow hard to use conf/config.properties file in android, I build up properties object hard coded for now
 *
 * @author matthiasneubert
 */
class ServicePlatformConfig {

	public static Properties getConfig(String rootPath, InputStream fileStream) {
		ServicePlatformConfig.analyzeClassPath();
		Properties config = new Properties();
		try {
			config.load(fileStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// create empty bundle dir (BasicServiceActivator will use it)
		File bundlesDir = new File(rootPath + "/felix/bundle");
		ServicePlatformConfig.clearDir(bundlesDir);
		// the file install watched dir for new bundles
		File newBundlesDir = new File(rootPath + "/felix/newbundle");
		ServicePlatformConfig.clearDir(newBundlesDir);
		// create felix cache dir
		File cacheDir = new File(rootPath + "/felix/cache");
		ServicePlatformConfig.clearDir(cacheDir);

		String rootDir = BundleCache.CACHE_ROOTDIR_PROP;
		String storageDir = FelixConstants.FRAMEWORK_STORAGE;
		String installDir = "felix.fileinstall.dir";

		config.setProperty(rootDir, MessageFormat.format(config.getProperty(rootDir), rootPath));
		config.setProperty(storageDir, MessageFormat.format(config.getProperty(storageDir), rootPath));
		config.setProperty(installDir, MessageFormat.format(config.getProperty(installDir), rootPath));
		return config;
	}

	// package scanner
	private static void analyzeClassPath() {
		PackageScanner pkgScanner = new PackageScanner();
		// set usage of classloader to avoid NPE in internal scanner of PackageScanner
		pkgScanner.useClassLoader(PackageScanner.class.getClassLoader().getParent());

		Collection<ExportPackage> exports = pkgScanner.select(
				PackageScanner.jars(
						PackageScanner.include("*.jar"),
						PackageScanner.exclude("felix.jar", "package*.jar")
				),
				PackageScanner.packages(
						PackageScanner.include(
								"org.*", "com.*", "javax.*", "android", "android.*", "com.android.*",
								"dalvik.*", "java.*", "junit.*", "org.apache.*", "org.json", "org.xml.*",
								"org.xmlpull.*", "org.w3c.*"
						)
				)
		).scan();
		// now fill analyzedExportString
		while (exports.iterator().hasNext()) {
			System.out.println("exports: " + exports.iterator().next().getPackageName());
		}
	}

	private static void clearDir(File dir) {
		if (dir.exists())
			delete(dir);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new IllegalStateException("Unable to create bundles dir");
			}
		}
	}

	private static void delete(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null)
				for (File f : files) {
					delete(f);
				}
		}
		file.delete();
	}
}
