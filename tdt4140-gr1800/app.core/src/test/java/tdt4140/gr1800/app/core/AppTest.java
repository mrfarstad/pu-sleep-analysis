package tdt4140.gr1800.app.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AppTest {

	private App app;
	
	@Before
	public void setUp() {
		app = new App();
	}

	@Test
	public void testNewDocument() {
		IDocumentStorage<File> documentStorage = app.getDocumentStorage();
		documentStorage.newDocument();
		Assert.assertFalse(app.getGeoLocationNames().iterator().hasNext());
		Assert.assertNull(documentStorage.getDocumentLocation());
	}

	protected void testLoadDocument(File file) {
		IDocumentStorage<File> documentStorage = app.getDocumentStorage();
		try {
			documentStorage.openDocument(file);
		} catch (IOException e) {
			Assert.fail("Couldn't open " + file);
		}
		Assert.assertEquals(file, documentStorage.getDocumentLocation());
		GeoLocationsPersistenceTest.testGeoLocationsDotJson(app.getGeoLocations((String[]) null));
	}
	
	@Test
	public void testLoadDocument() {
		URL url = getClass().getResource("geoLocations.json");
		Assert.assertEquals("Not file URL", "file", url.getProtocol()); 
		File file = new File(url.getPath());
		testLoadDocument(file);
	}

	@Test
	public void testSaveDocument() {
		testLoadDocument();
		try {
			IDocumentStorage<File> documentStorage = app.getDocumentStorage();
			File tempFile = File.createTempFile("geoLocations", ".json");
			tempFile.deleteOnExit();
			documentStorage.setDocumentLocation(tempFile);
			documentStorage.saveDocument();
			testLoadDocument(tempFile);
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
