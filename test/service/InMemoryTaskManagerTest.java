package service;



class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

   public InMemoryTaskManager createTaskManager() {

      return (InMemoryTaskManager) Managers.getDefault();
   }

}