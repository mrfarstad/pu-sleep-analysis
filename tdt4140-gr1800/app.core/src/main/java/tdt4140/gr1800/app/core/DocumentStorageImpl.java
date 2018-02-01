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

	protected void setDocumentAndLocation(D document, L documentLocation) {
		setDocument(document);
		setDocumentLocation(documentLocation);
	}
	
	protected abstract D getDocument();
	protected abstract void setDocument(D document);

	protected abstract D createDocument();
	protected abstract D loadDocument(L storage) throws IOException;
	protected abstract void storeDocument(D document, L storage) throws IOException;
	
	@Override
	public void newDocument() {
		setDocumentAndLocation(createDocument(), null);
	}

	@Override
	public void openDocument(L storage) throws IOException {
		setDocumentAndLocation(loadDocument(storage), storage);
	}

	@Override
	public void saveDocument() throws IOException {
		storeDocument(getDocument(), getDocumentLocation());
	}

	public void saveDocumentAs(L documentLocation) throws IOException {
		L oldDocumentLocation = getDocumentLocation();
		setDocumentLocation(documentLocation);
		try {
			saveDocument();
		} catch (IOException e) {
			setDocumentLocation(oldDocumentLocation);
			throw e;
		}
	}
	
	public void saveCopyAs(L documentLocation) throws IOException {
		storeDocument(getDocument(), documentLocation);
	}
}
