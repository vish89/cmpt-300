package myseaapp;

/**
 * A class for simulating a process within the whole OS simulation library.
 * @author oabahuss && fredQin
 */
public class Process {
    private int id;
    private int priority;
    private int state; //1 for ready, 2 for blocked, 3 for running.
    //private int startingAddress;
    private int last_address;
    private int parentId;
    private int pc; 
    
    /**
     * A constructor for a brand new process. State begins at 1, and pc = 0.
     * @param id
     * @param priority
     * @param startingAddress Memory starting address.
     * @param length 
     */
      Process(int id, int priority, int pc, int last_address){
        this.id = id;
        this.priority = priority;
        this.state = state;
        //this.startingAddress = startingAddress;
        this.last_address = last_address;
        this.parentId = 0;
        this.state = 1;
        this.pc = pc;
    }
    
    /**
     * Creates a copy of the parent, with different id.
     * @param parent The creating parent process.
     * @param id The child's id, assigned by simulator.
     */
    public void Process(Process parent, int id){
        this.priority = parent.getPriority();
        this.parentId = parent.getId();
        //this.startingAddress = parent.getStartingAddress();
        this.last_address = parent.getLength();
        
        
        this.id = id;
        this.state = 1;
    }
    
    /**
     * @return The memory address of the next instruction.
     */
    public int getNextInstruction(){
        if(pc>last_address)
            return -1;
        return this.pc++;
    }

    
    public int getId() {
        return this.id;
    }
    public int getPriority() {
        return this.priority;
    }

//    public int getStartingAddress() {
//        return this.startingAddress;
//    }

    public int getLength() {
        return (last_address-pc);
    }



    public int getParentId() {
        return this.parentId;
    }

    public void block() {
        this.state = 2;
    }
    
    public void ready() {
        this.state = 1;
    }
    
    public void run() {
        this.state = 3;
    }
}
