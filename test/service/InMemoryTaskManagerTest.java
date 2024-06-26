package service;

import org.junit.jupiter.api.DisplayName;





@DisplayName("Тестируем Менеджер задач")
class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

   public InMemoryTaskManager createTaskManager() {

      return (InMemoryTaskManager) Managers.getDefault();
   }

}