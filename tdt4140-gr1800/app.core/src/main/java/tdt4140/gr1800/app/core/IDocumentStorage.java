package tdt4140.gr1800.app.core;

import java.io.IOException;

public interface IDocumentStorage<L> {
	public L getDocumentLocation();
	public void setDocumentLocation(L documentLocation);

	public void newDocument();
	public void openDocument(L documentLocation) throws IOException;
	public void saveDocument() throws IOException;
}
