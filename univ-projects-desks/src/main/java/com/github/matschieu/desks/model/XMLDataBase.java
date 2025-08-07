package com.github.matschieu.desks.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.github.matschieu.desks.model.util.Desk;
import com.github.matschieu.desks.model.util.Person;
import com.github.matschieu.desks.model.util.Reservation;
import com.github.matschieu.desks.model.util.Week;

/**
 * @author Matschieu
 */
public class XMLDataBase extends DataBase {

	public static final String DEFAULT_XML_DESK_FILE = "xml/desks.xml";
	public static final String DEFAULT_XML_RESV_FILE = "xml/reservations.xml";
	public static final String DEFAULT_XML_PERS_FILE = "xml/persons.xml";

	private String xmlDeskFile;	// Desks XML file
	private String xmlResvFile;	// Reservations XML file
	private String xmlPersFile;	// Persons XML file

	private SAXBuilder builder;
	private Document deskDoc;	// Desks
	private Document resvDoc;	// Reservations
	private Document persDoc;	// People

	public XMLDataBase() {
		this.xmlDeskFile = DEFAULT_XML_DESK_FILE;
		this.xmlResvFile = DEFAULT_XML_RESV_FILE;
		this.xmlPersFile = DEFAULT_XML_PERS_FILE;
		this.builder = new SAXBuilder(true);
	}

	public XMLDataBase(String xmlDeskFile, String xmlPersFile, String xmlResvFile) throws JDOMException, IOException {
		this.xmlDeskFile = xmlDeskFile;
		this.xmlResvFile = xmlResvFile;
		this.xmlPersFile = xmlPersFile;

		this.builder = new SAXBuilder(true);
		this.deskDoc = builder.build(this.xmlDeskFile);
		this.resvDoc = builder.build(this.xmlResvFile);
		this.persDoc = builder.build(this.xmlPersFile);
	}

	public String getXmlDeskFile() {
		return xmlDeskFile;
	}

	public void setXmlDeskFile(String xmlDeskFile) throws JDOMException, IOException {
		this.xmlDeskFile = xmlDeskFile;
		this.deskDoc = builder.build(xmlDeskFile);
	}

	public String getXmlResvFile() {
		return xmlResvFile;
	}

	public void setXmlResvFile(String xmlResvFile) throws JDOMException, IOException {
		this.xmlResvFile = xmlResvFile;
		this.resvDoc = this.builder.build(xmlResvFile);
	}

	public String getXmlPersFile() {
		return xmlPersFile;
	}

	public void setXmlPersFile(String xmlPersFile) throws JDOMException, IOException {
		this.xmlPersFile = xmlPersFile;
		this.persDoc = this.builder.build(xmlPersFile);
	}

	/**
	 * Creates the database (first use)
	 * @return true if the database is correctly created
	 */
	@Override
	public boolean createDataBase() {
		try {
			final String deskDtdFileName = "desks.dtd";
			final String resvDtdFileName = "reservations.dtd";
			final String persDtdFileName = "persons.dtd";

			String deskDtd = "<!ELEMENT deskslist (desk*)>\n\n";
			deskDtd += "<!ELEMENT desk (id, number, building, room, description)>\n";
			deskDtd += "<!ELEMENT id (#PCDATA)>\n";
			deskDtd += "<!ELEMENT number (#PCDATA)>\n";
			deskDtd += "<!ELEMENT building (#PCDATA)>\n";
			deskDtd += "<!ELEMENT room (#PCDATA)>\n";
			deskDtd += "<!ELEMENT description (#PCDATA)>\n";

			String resvDtd = "<!ELEMENT reservationslist (reservation*)>\n\n";
			resvDtd += "<!ELEMENT reservation (resid, deskid, managerid, traineeid, startday, startyear, endday, endyear)>\n";
			resvDtd += "<!ELEMENT resid (#PCDATA)>\n";
			resvDtd += "<!ELEMENT deskid (#PCDATA)>\n";
			resvDtd += "<!ELEMENT managerid (#PCDATA)>\n";
			resvDtd += "<!ELEMENT traineeid (#PCDATA)>\n";
			resvDtd += "<!ELEMENT startday (#PCDATA)>\n";
			resvDtd += "<!ELEMENT startyear (#PCDATA)>\n";
			resvDtd += "<!ELEMENT endday (#PCDATA)>\n";
			resvDtd += "<!ELEMENT endyear (#PCDATA)>\n";

			String persDtd = "<!ELEMENT personslist (person*)>\n\n";
			persDtd += "<!ELEMENT person (id, name, firstname, post, trainee)>\n";
			persDtd += "<!ELEMENT id (#PCDATA)>\n";
			persDtd += "<!ELEMENT name (#PCDATA)>\n";
			persDtd += "<!ELEMENT firstname (#PCDATA)>\n";
			persDtd += "<!ELEMENT post (#PCDATA)>\n";
			persDtd += "<!ELEMENT trainee (#PCDATA)>\n";

			File xmlDir = (new File(this.xmlDeskFile)).getParentFile();
			xmlDir.mkdir();

			File desksDtdFile = new File(xmlDir, deskDtdFileName);
			File ReservationsDtdFile = new File(xmlDir, resvDtdFileName);
			File PersonsDtdFile = new File(xmlDir, persDtdFileName);

			desksDtdFile.createNewFile();
			ReservationsDtdFile.createNewFile();
			PersonsDtdFile.createNewFile();

			(new FileOutputStream(desksDtdFile)).write(deskDtd.getBytes());
			(new FileOutputStream(ReservationsDtdFile)).write(resvDtd.getBytes());
			(new FileOutputStream(PersonsDtdFile)).write(persDtd.getBytes());

			this.deskDoc = new Document();
			this.resvDoc = new Document();
			this.persDoc = new Document();

			this.deskDoc.setRootElement(new Element("deskslist"));
			this.resvDoc.setRootElement(new Element("reservationslist"));
			this.persDoc.setRootElement(new Element("personslist"));

			this.deskDoc.setDocType(new DocType("deskslist", deskDtdFileName));
			this.resvDoc.setDocType(new DocType("reservationslist", resvDtdFileName));
			this.persDoc.setDocType(new DocType("personslist", persDtdFileName));

			this.save(this.deskDoc, this.xmlDeskFile);
			this.save(this.resvDoc, this.xmlResvFile);
			this.save(this.persDoc, this.xmlPersFile);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private int nextDeskId() {
		int max = 0;
		Element root = this.deskDoc.getRootElement();
		for(Object o : root.getChildren("desk")) {
			Element e = (Element)o;
			int tmp = Integer.parseInt(e.getChildText("id"));
			if (max < tmp)
				max = tmp;
		}
		return max + 1;
	}

	private int nextPersonId() {
		int max = 0;
		Element root = this.persDoc.getRootElement();
		for(Object o : root.getChildren("person")) {
			Element e = (Element)o;
			int tmp = Integer.parseInt(e.getChildText("id"));
			if (max < tmp)
				max = tmp;
		}
		return max + 1;
	}

	private int nextReservationId() {
		int max = 0;
		Element root = this.resvDoc.getRootElement();
		for(Object o : root.getChildren("reservation")) {
			Element e = (Element)o;
			int tmp = Integer.parseInt(e.getChildText("resid"));
			if (max < tmp)
				max = tmp;
		}
		return max + 1;
	}

	/**
	 * Add the desk d to the dataBase
	 * @param d desk to add
	 */
	@Override
	public void addDesk(Desk d) {
		d.setId(this.nextDeskId());

		Element root = this.deskDoc.getRootElement();

		Element desk = new Element("desk");

		Element id = new Element("id");
		Element number = new Element("number");
		Element building = new Element("building");
		Element room = new Element("room");
		Element description = new Element("description");

		id.setText("" + d.getId());
		number.setText(d.getNumber());
		building.setText(d.getBuilding());
		room.setText(d.getRoom());
		description.setText(d.getDescription());

		desk.addContent(id);
		desk.addContent(number);
		desk.addContent(building);
		desk.addContent(room);
		desk.addContent(description);

		root.addContent(desk);

		this.save(this.deskDoc, this.xmlDeskFile);

		setChanged();
		notifyObservers();
	}

	/**
	 * Updates the desk d in the dataBase
	 * @param d desk to update
	 */
	@Override
	public void updateDesk(Desk d) {
		Element root = this.deskDoc.getRootElement();

		for(Object o : root.getChildren("desk")) {
			Element e = (Element)o;
			int id = Integer.parseInt(e.getChildText("id"));
			if (id == d.getId()) {
				e.getChild("number").setText(d.getNumber());
				e.getChild("building").setText(d.getBuilding());
				e.getChild("room").setText(d.getRoom());
				e.getChild("description").setText(d.getDescription());
				break;
			}
		}

		this.save(this.deskDoc, this.xmlDeskFile);

		setChanged();
		notifyObservers();
	}

	/**
	 * Removes the desk d of the dataBase
	 * Removes also all reservations for this desk
	 * @param d desk to remove
	 */
	@Override
	public void removeDesk(Desk d) {
		Element root = this.deskDoc.getRootElement();

		for(Reservation r : this.getReservations(d)) {
			this.removeReservation(r);
		}

		Element tmp = null;
		for(Object o : root.getChildren("desk")) {
			Element e = (Element)o;
			int id = Integer.parseInt(e.getChildText("id"));
			if (id == d.getId()) {
				tmp = e;
				break;
			}
		}
		if (tmp != null)
			root.removeContent(tmp);

		this.save(this.deskDoc, this.xmlDeskFile);

		setChanged();
		notifyObservers();
	}

	/**
	 * Checks if a desk already exists
	 * @return true if a desk with the same id exists
	 */
	@Override
	public boolean deskExists(Desk d) {
		if (d == null)
			return false;
		Element root = this.deskDoc.getRootElement();
		for(Object o : root.getChildren("desk")) {
			Element e = (Element)o;
			int id = Integer.parseInt(e.getChildText("id"));
			if (id == d.getId())
				return true;
		}
		return false;
	}

	/**
	 * Returns all desks existing
	 * @return List<Desk> a list of all desks
	 */
	@Override
	public List<Desk> getAllDesk() {
		List<Desk> l = new LinkedList<Desk>();

		Element root = this.deskDoc.getRootElement();
		for(Object o : root.getChildren("desk")) {
			Element e = (Element)o;
			int id = Integer.parseInt(e.getChildText("id"));
			String number = e.getChildText("number");
			String building = e.getChildText("building");
			String room = e.getChildText("room");
			String description = e.getChildText("description");
			l.add(new Desk(id, number, building, room, description));
		}

		Collections.sort(l);

		return l;
	}

	/**
	 * Get the desk with the desk id deskId
	 * @param deskId the desk id desk to get
	 * @return the desk with the desk id deskId
	 */
	@Override
	public Desk getDesk(String deskId) {
		Element root = this.deskDoc.getRootElement();

		for(Object o : root.getChildren("desk")) {
			Element e = (Element)o;
			if (e.getChildText("id").equals(deskId)) {
				int id = Integer.parseInt(e.getChildText("id"));
				String number = e.getChildText("number");
				String building = e.getChildText("building");
				String room = e.getChildText("room");
				String description = e.getChildText("description");
				return new Desk(id, number, building, room, description);
			}
		}

		return null;
	}

	/**
	 * Add the person p to the dataBase
	 * @param d person to add
	 */
	@Override
	public void addPerson(Person p) {
		p.setId(this.nextPersonId());

		Element root = this.persDoc.getRootElement();

		Element person = new Element("person");

		Element id = new Element("id");
		Element name = new Element("name");
		Element firstName = new Element("firstname");
		Element post = new Element("post");
		Element trainee = new Element("trainee");

		id.setText("" + p.getId());
		name.setText(p.getName());
		firstName.setText(p.getFirstName());
		post.setText(p.getPost());
		trainee.setText((p.isTrainee() ? "1" : "0"));

		person.addContent(id);
		person.addContent(name);
		person.addContent(firstName);
		person.addContent(post);
		person.addContent(trainee);

		root.addContent(person);

		this.save(this.persDoc, this.xmlPersFile);

		setChanged();
		notifyObservers();
	}

	/**
	 * Updates the person p in the dataBase
	 * @param p person to update
	 */
	@Override
	public void updatePerson(Person p) {
		Element root = this.persDoc.getRootElement();

		for(Object o : root.getChildren("person")) {
			Element e = (Element)o;
			int id = Integer.parseInt(e.getChildText("id"));
			if (id == p.getId()) {
				e.getChild("name").setText(p.getName());
				e.getChild("firstname").setText(p.getFirstName());
				e.getChild("post").setText(p.getPost());
				e.getChild("trainee").setText((p.isTrainee() ? "1" : "0"));
				break;
			}
		}

		this.save(this.persDoc, this.xmlPersFile);

		setChanged();
		notifyObservers();
	}

	/**
	 * Removes the person d of the dataBase
	 * Removes also all reservations containing this person
	 * @param p person to remove
	 */
	@Override
	public void removePerson(Person p) {
		Element root = this.persDoc.getRootElement();

		for(Reservation r : this.getReservations(p)) {
			this.removeReservation(r);
		}

		Element tmp = null;
		for(Object o : root.getChildren("person")) {
			Element e = (Element)o;
			int id = Integer.parseInt(e.getChildText("id"));
			if (id == p.getId()) {
				tmp = e;
				break;
			}
		}
		if (tmp != null)
			root.removeContent(tmp);

		this.save(this.persDoc, this.xmlPersFile);

		setChanged();
		notifyObservers();

	}

	/**
	 * Returns the person with the person id personId
	 * @param personId the person id desk to get
	 * @return Person the person with the id personId
	 */
	@Override
	public Person getPerson(String personId) {
		Element root = this.persDoc.getRootElement();
		for(Object o : root.getChildren("person")) {
			Element e = (Element)o;
			if (e.getChildText("id").equals(personId)) {
				String name = e.getChildText("name");
				String firstname = e.getChildText("firstname");
				String post = e.getChildText("post");
				boolean trainee = Integer.parseInt(e.getChildText("trainee")) == 1;
				return new Person(Integer.parseInt(personId), name, firstname, post, trainee);
			}
		}
		return null;
	}

	/**
	 * Checks if a person already exists
	 * @return true if exist
	 */
	@Override
	public boolean personExists(Person p) {
		if (p == null)
			return false;
		Element root = this.persDoc.getRootElement();
		for(Object o : root.getChildren("person")) {
			Element e = (Element)o;
			int id = Integer.parseInt(e.getChildText("id"));
			if (id == p.getId())
				return true;
		}
		return false;
	}

	/**
	 * Returns all persons existing
	 * @return List<Person> a list of all persons
	 */
	@Override
	public List<Person> getAllPerson() {
		List<Person> l = new LinkedList<Person>();

		Element root = this.persDoc.getRootElement();
		for(Object o : root.getChildren("person")) {
			Element e = (Element)o;
			int id = Integer.parseInt(e.getChildText("id"));
			String name = e.getChildText("name");
			String firstname = e.getChildText("firstname");
			String post = e.getChildText("post");
			boolean trainee = Integer.parseInt(e.getChildText("trainee")) == 1;
			l.add(new Person(id, name, firstname, post, trainee));
		}

		Collections.sort(l);

		return l;
	}

	/**
	 * Returns all persons existing who are trainee or not
	 * @param trainee select only trainees if true, else other persons
	 * @return List<Person> a list of persons
	 */
	@Override
	public List<Person> getAllPerson(boolean trainee) {
		List<Person> l = new LinkedList<Person>();

		Element root = this.persDoc.getRootElement();
		for(Object o : root.getChildren("person")) {
			Element e = (Element)o;
			int id = Integer.parseInt(e.getChildText("id"));
			String name = e.getChildText("name");
			String firstname = e.getChildText("firstname");
			String post = e.getChildText("post");
			boolean istrainee = Integer.parseInt(e.getChildText("trainee")) == 1;
			if (trainee && istrainee) {
				l.add(new Person(id, name, firstname, post, istrainee));
			}
			else if (!trainee && !istrainee) {
				l.add(new Person(id, name, firstname, post, istrainee));
			}
		}

		Collections.sort(l);

		return l;
	}

	/**
	 * Checks if there's an homonyme in the database
	 * @return true if the person p has an homonyme
	 */
	@Override
	public boolean containsHomonyme(Person p) {
		if (p == null)
			return false;
		Element root = this.persDoc.getRootElement();
		for(Object o : root.getChildren("person")) {
			Element e = (Element)o;
			//int id = Integer.parseInt(e.getChildText("id"));
			if (p.getName().toUpperCase().equals(e.getChildText("name").toUpperCase()) &&
				p.getFirstName().toUpperCase().equals(e.getChildText("firstname").toUpperCase()))
				return true;
		}
		return false;
	}

	/**
	 * Checks if a reservation already exists
	 * @return true if a reservation with the same id exists
	 */
	@Override
	public boolean reservationExists(Reservation r) {
		if (r == null)
			return false;
		Element root = this.resvDoc.getRootElement();
		for(Object o : root.getChildren("reservation")) {
			Element e = (Element)o;
			if (e.getChildText("resid").equals(r.getId()))
				return true;
		}
		return false;
	}

	/**
	 * Returns the reservation with the id resId
	 * @param resId the id reservation to get
	 * @return the reservation with the id resId
	 */
	@Override
	public Reservation getReservation(String resId){

		Element root = this.resvDoc.getRootElement();
		for(Object o : root.getChildren("reservation")) {
			Element e = (Element)o;
			if (e.getChildText("resid").equals(resId)) {
				String resid = e.getChildText("resid");
				String deskid = e.getChildText("deskid");
				String managerid = e.getChildText("managerid");
				String traineeid = e.getChildText("traineeid");
				int startday = Integer.parseInt(e.getChildText("startday"));
				int startyear = Integer.parseInt(e.getChildText("startyear"));
				int endday = Integer.parseInt(e.getChildText("endday"));
				int endyear = Integer.parseInt(e.getChildText("endyear"));
				return new Reservation(resid, getDesk(deskid), getPerson(managerid), getPerson(traineeid), new Week(startday, startyear), new Week(endday, endyear));
			}
		}
		return null;
	}

	/**
	 * Adds the reservation r to the dataBase
	 * @param r reservation to add
	 */
	@Override
	public void addReservation(Reservation r) {
		r.setId(""+this.nextReservationId());

		Element root = this.resvDoc.getRootElement();

		Element reservation = new Element("reservation");

		Element resid = new Element("resid");
		Element deskid = new Element("deskid");
		Element managerid = new Element("managerid");
		Element traineeid = new Element("traineeid");
		Element startday = new Element("startday");
		Element startyear = new Element("startyear");
		Element endday = new Element("endday");
		Element endyear = new Element("endyear");

		resid.setText(r.getId());
		deskid.setText("" + r.getDesk().getId());
		managerid.setText(""+ r.getManager().getId());
		traineeid.setText("" + r.getTrainee().getId());
		startday.setText(""+r.getStart().getWeekNumber());
		startyear.setText(""+r.getStart().getYear());
		endday.setText(""+r.getEnd().getWeekNumber());
		endyear.setText(""+r.getEnd().getYear());

		reservation.addContent(resid);
		reservation.addContent(deskid);
		reservation.addContent(managerid);
		reservation.addContent(traineeid);
		reservation.addContent(startday);
		reservation.addContent(startyear);
		reservation.addContent(endday);
		reservation.addContent(endyear);

		root.addContent(reservation);

		this.save(this.resvDoc, this.xmlResvFile);

		setChanged();
		notifyObservers();
	}

	/**
	 * Updates the reservation r in the dataBase
	 * @param r reservation to update
	 */
	@Override
	public void updateReservation(Reservation r) {
		Element root = this.resvDoc.getRootElement();

		for(Object o : root.getChildren("reservation")) {
			Element e = (Element)o;
			if (e.getChildText("resid").equals(r.getId())) {
				e.getChild("resid").setText(r.getId());
				e.getChild("deskid").setText("" + r.getDesk().getId());
				e.getChild("managerid").setText(""+r.getManager().getId());
				e.getChild("traineeid").setText(""+r.getTrainee().getId());
				e.getChild("startday").setText(""+r.getStart().getWeekNumber());
				e.getChild("startyear").setText(""+r.getStart().getYear());
				e.getChild("endday").setText(""+r.getEnd().getWeekNumber());
				e.getChild("endyear").setText(""+r.getEnd().getYear());
				break;
			}
		}

		this.save(this.resvDoc, this.xmlResvFile);

		setChanged();
		notifyObservers();
	}

	/**
	 * Remove the reservation of the dataBase
	 * @param r reservation to remove
	 */
	@Override
	public void removeReservation(Reservation r){
		Element root = this.resvDoc.getRootElement();

		Element tmp = null;
		for(Object o : root.getChildren("reservation")) {
			Element e = (Element)o;
			if (e.getChildText("resid").equals(r.getId())) {
				tmp = e;
				break;
			}
		}
		if (tmp != null)
			root.removeContent(tmp);

		this.save(this.resvDoc, this.xmlResvFile);

		setChanged();
		notifyObservers();
	}

	/**
	 * Returns all reservations existing
	 * @return List<Reservation> a list of all reservations
	 */
	@Override
	public List<Reservation> getAllReservation() {
		List<Reservation> l = new LinkedList<Reservation>();

		Element root = this.resvDoc.getRootElement();
		for(Object o : root.getChildren("reservation")) {
			Element e = (Element)o;
			String resid = e.getChildText("resid");
			String deskid = e.getChildText("deskid");
			String managerid = e.getChildText("managerid");
			String traineeid = e.getChildText("traineeid");
			int startday = Integer.parseInt(e.getChildText("startday"));
			int startyear = Integer.parseInt(e.getChildText("startyear"));
			int endday = Integer.parseInt(e.getChildText("endday"));
			int endyear = Integer.parseInt(e.getChildText("endyear"));
			l.add(new Reservation(resid, getDesk(deskid), getPerson(managerid), getPerson(traineeid), new Week(startday, startyear), new Week(endday, endyear)));
		}

		Collections.sort(l);

		return l;
	}

	/**
	 * Returns all reservations existing at a specified week
	 * @return List<Reservation> a list of all reservations
	 */
	@Override
	public List<Reservation> getAllReservation(Week week) {
		List<Reservation> l = new LinkedList<Reservation>();
		for (Reservation res : getAllReservation()) {
			if (res.getStart().compareTo(week) <= 0 && res.getEnd().compareTo(week) >= 0){
				l.add(res);
			}
		}

		Collections.sort(l);

		return l;
	}

	/**
	 * Returns all reservations where a person appears
	 * @param p the person (trainee or manager)
	 * @return List<Reservation>
	 */
	@Override
	public List<Reservation> getReservations(Person p) {
		List<Reservation> l = new LinkedList<Reservation>();

		Element root = this.resvDoc.getRootElement();
		for(Object o : root.getChildren("reservation")) {
			Element e = (Element)o;
			String resid = e.getChildText("resid");
			Desk desk = getDesk(e.getChildText("deskid"));
			Person manager = getPerson(e.getChildText("managerid"));
			Person trainee = getPerson(e.getChildText("traineeid"));
			int startday = Integer.parseInt(e.getChildText("startday"));
			int startyear = Integer.parseInt(e.getChildText("startyear"));
			int endday = Integer.parseInt(e.getChildText("endday"));
			int endyear = Integer.parseInt(e.getChildText("endyear"));
			if (manager != null && trainee != null &&
					(manager.getId() == p.getId() || trainee.getId() == p.getId()))
				l.add(new Reservation(resid, desk, manager, trainee, new Week(startday, startyear), new Week(endday, endyear)));
		}

		Collections.sort(l);

		return l;
	}

	/**
	 * Returns all reservations for a desk
	 * @param d the desk
	 * @return List<Reservation>
	 */
	@Override
	public List<Reservation> getReservations(Desk d) {
		List<Reservation> l = new LinkedList<Reservation>();

		Element root = this.resvDoc.getRootElement();
		for(Object o : root.getChildren("reservation")) {
			Element e = (Element)o;
			String resid = e.getChildText("resid");
			Desk desk = getDesk(e.getChildText("deskid"));
			Person manager = getPerson(e.getChildText("managerid"));
			Person trainee = getPerson(e.getChildText("traineeid"));
			int startday = Integer.parseInt(e.getChildText("startday"));
			int startyear = Integer.parseInt(e.getChildText("startyear"));
			int endday = Integer.parseInt(e.getChildText("endday"));
			int endyear = Integer.parseInt(e.getChildText("endyear"));
			if (d != null && desk.getId() == d.getId())
				l.add(new Reservation(resid, desk, manager, trainee, new Week(startday, startyear), new Week(endday, endyear)));
		}

		Collections.sort(l);

		return l;
	}

	/**
	 * Save a document in a XML file
	 * @param doc the document (content) to save
	 * @param xmlFileName the XML file name
	 */
	private void save(Document doc, String xmlFileName) {
		try {
			XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
			xmlOut.output(doc, new FileOutputStream(new File(xmlFileName)));
		} catch (IOException e) {
			System.err.println ("Erreur : impossible d'enregistrer le fichier " + xmlFileName + " / " + e.getMessage());
		}
	}
}
