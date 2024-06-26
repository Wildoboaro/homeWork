package re.zip.famely_tree.model.famely_tree;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import re.zip.famely_tree.model.famely_tree.iterators.FamelyTreeElementIterator;
import re.zip.famely_tree.model.human.comparators.FamelyTreeElementComparatorByBirthDate;
import re.zip.famely_tree.model.human.comparators.FamelyTreeElementComparatorByFamelyName;
import re.zip.famely_tree.model.human.comparators.FamelyTreeElementComparatorByFirstName;

public class FamelyTree<E extends FamelyTreeElement<E>> implements Serializable, Iterable<E>{

    private long humanId;
    private final List<E> humanList;

    public FamelyTree(List<E> humanList){
        this.humanList = humanList;
    }
    
    public FamelyTree(){
        humanList = new ArrayList<>();
    }

    public boolean addToFamely(E human){
        if (human == null) {
            return false;
        }
        if (!humanList.contains(human)){
            humanList.add(human);
            human.setIdNo(++humanId);
            addHumanToCildren(human);
            addHumanToParents(human);
            return true;
        }
        return false;
    }

    private void addHumanToParents(E human){
        for (E parent : human.getListParents()) {
           parent.addACild(human); 
        }
    }

    private void addHumanToCildren(E human){
        for (E child : human.getChildren() ) {
            child.addAParent(human); 
         }
    }

    public List<E> getSiblingsList (int humanId){
        E human = searchHumanById(humanId);
        if (human == null){
            return null;
        }
        List<E> listSiblings = new ArrayList<>();
        for (E parent : human.getListParents()) {
            for (E child : parent.getChildren()) {
                if (!child.equals(human)){
                    listSiblings.add(child);
                }
            }
        }
    return listSiblings;
    }

    public List<E> searchByNane(String name){
        List<E> ListByName = new ArrayList<>();
        for (E human : humanList){
            if (human.getFirstName().equals(name)){
                ListByName.add(human);
            }
        }
    return ListByName;
    }

    public boolean setWeddding (Integer partner1ID, Integer partner2ID, Integer getFamelyName){
        if (checkID(partner1ID) && checkID(partner2ID)){
            E partner1 = searchHumanById(partner1ID);
            E partner2 = searchHumanById(partner2ID);
            return setWeddding(partner1, partner2, getFamelyName);
        }
        return false;
    }

    public boolean setWeddding (Integer partner1ID, Integer partner2ID){
        return setWeddding(partner1ID, partner2ID, 0);
    }

//TODO необходимо что-то сделать с перводом мужских фамилий в женские и обратно

    public boolean setWeddding (E partner1, E partner2, Integer getFamelyName){
        if (partner1.getSpouse() == null && partner1.getSpouse() == null){
            partner1.setSpouse(partner2);
            partner2.setSpouse(partner1);
            if (getFamelyName == 1){
                partner2.setFamelyName(partner1.getFamelyName() + " (" + partner2.getFamelyName() + ")");
            }
            else{
                if (getFamelyName == 2){
                    partner1.setFamelyName(partner2.getFamelyName() + " (" + partner1.getFamelyName() + ")");
                }
            }
            return true;
        }
        System.out.println("Не все парнеры свободны.");
        return false;
    }

    public boolean setWeddding (E partner1, E partner2, String newFamelyName){
        if (partner1.getSpouse() == null && partner1.getSpouse() == null){
            partner1.setSpouse(partner2);
            partner2.setSpouse(partner1);
            partner2.setFamelyName(newFamelyName + " (" + partner2.getFamelyName() + ")");
            partner1.setFamelyName(newFamelyName + " (" + partner1.getFamelyName() + ")");
            return true;
        }
        return false;
    }

    public boolean setWeddding (E partner1, E partner2){
        if (partner1.getSpouse() == null && partner1.getSpouse() == null){
            partner1.setSpouse(partner2);
            partner2.setSpouse(partner1);
            return true;
        }
        return false;
    }

    public boolean setDivorse (Integer partner1ID, Integer partner2ID){
        if (checkID(partner1ID) && checkID(partner2ID)){
            E partner1 = searchHumanById(partner1ID);
            E partner2 = searchHumanById(partner2ID);
            return setDivorse(partner1, partner2);
        }
        return false;
    }

    public boolean setDivorse (E partner1, E partner2){
        if (partner1.getSpouseInfo() != null && partner1.getSpouseInfo() != null){
            partner1.setSpouse(null);
            partner1.setSpouse(null);
            return true;
        }
        return false;
    }

    public boolean removeFromFamely (long humanID){
        if (checkID(humanID)){
            E human = searchHumanById(humanID);
            return removeFromFamely(human);
        }
        return false;
    }

    public boolean removeFromFamely(E human){
        if (humanList.contains(human)){
            return humanList.remove(human);
        }
        return false;
    }

    private boolean checkID(long id){
        return id < humanId && id > 0;
    }

    public E searchHumanById(long id){
        if (checkID(id)){
            for (E human: humanList){
                if (human.getIdNo() == id) {
                    return human;
                }
            }
        }
    return null;
    }

    public void sortByFamelyName() {
        humanList.sort(new FamelyTreeElementComparatorByFamelyName<>());
    }

    public void sortByFirstName() {
        humanList.sort(new  FamelyTreeElementComparatorByFirstName<>());
    }

    public void sortByBirthDate() {
        humanList.sort(new  FamelyTreeElementComparatorByBirthDate<>());
    }

    public void setSiblingsRelationship(Integer sibling1Id, Integer sibling2Id) {
        E sibling1 = searchHumanById(sibling1Id);
        E sibling2 = searchHumanById(sibling2Id);
        setSiblingsRelationship(sibling1, sibling2);
    }

    public void setSiblingsRelationship(E sibling1, E sibling2) {
        if (sibling1 != null && sibling2 != null) {
            if (sibling1.getFather() == null && sibling1.getMother() == null) {
            sibling1.getFather().addAChild(sibling2);
            sibling1.getMother().addAChild(sibling2);
            }
            else if (sibling2.getFather() == null && sibling2.getMother() == null) {
                sibling2.getFather().addAChild(sibling1);
                sibling2.getMother().addAChild(sibling1);
                }
            else {
                System.out.println("У  указанных братьев(сестер) есть обозначенные родители. Установка такой связи невозможна");
            }
        }
    }
        
    public void setChildParentRelationship(Integer parentID, Integer childID) {
        E parent = searchHumanById(parentID);
        E child = searchHumanById(childID);
        setChidParentRelationship(parent, child);

    }

    public void setChidParentRelationship(E parent, E child) {
        if (parent != null && child != null) {
            parent.addAChild(child);
            child.addAParent(parent);
            if (parent.getSpouse() != null) {
                parent.getSpouse().addAChild(child);
                child.addAParent(parent.getSpouse());
            }
        } else {
            System.out.println("Родитель или ребенок не найден.");
        }
    }
    
    @Override
    public Iterator<E> iterator() {   
        return new FamelyTreeElementIterator<>(humanList);
    }

    public String getFamelyListInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nСуществ семье: ");
        stringBuilder.append(humanList.size());
        stringBuilder.append("\n");
        for (E human : humanList){
            stringBuilder.append(human);
            stringBuilder.append("\n");
        } 
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return getFamelyListInfo();
    }

}
