package tdt4140.gr1800.app.core;

import java.io.IOException;

public abstract class DocumentStorageImpl<D, L> implements IDocumentStorage<L> {

	private L documentLocation;

	@Override
	public L getDocumentLocation() {
		return documentLocation;
	}

	@Override
	public void setDocumentLocation(L documentLocation) {
		this.documentLocation = documentLocation;
	}

	protected abstract D getDocument();
	protected abstract void setDocument(D document);

	protected abstract D createDocument();
	protected abstract D loadDocument(L storage) throws IOException;
	protected abstract void storeDocument(D document, L storage) throws IOException;
	
	@Override
	public void newDocument() {
		setDocument(createDocument());
	}

	@Override
	public void openDocument(L storage) throws IOException {
		setDocument(loadDocument(storage));
	}

	@Override
	public void saveDocument() throws IOException {
		storeDocument(getDocument(), getDocumentLocation());
	}

	public void saveDocumentAs(L documentLocation) throws IOException {
		setDocumentLocation(documentLocation);
		saveDocument();
	}
	
	public void saveCopyAs(L documentLocation) throws IOException {
		storeDocument(getDocument(), documentLocation);
	}
}
