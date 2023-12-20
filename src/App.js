import AddExam from "./Component/AddExam";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";
import 'bootstrap-icons/font/bootstrap-icons.css';
import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import AddTopic from './Component/AddTopic'
import AddQuestion from './Component/AddQuestion'
import Header from './Component/Header'
import Footer from './Component/Footer'
import ViewQuestion from './Component/ViewQuestion'
import UpdateQuestion from './Component/UpdateQuestion'
import TopicDisplay from './Component/TopicsDisplay'
import Welcome from './Component/Welcome'
import Test from "./Component/Test";






function App() {
  return (
    <>

      <Header />

      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Welcome />} />
          <Route path='/add-exam' element={<AddExam />} />
          <Route path='/add-topic' element={<AddTopic />} />
          <Route path='/add-question' element={<AddQuestion />} />
          <Route path='/view-question' element={<ViewQuestion />} />
          <Route path='/update-question' element={<UpdateQuestion />} />
          <Route path='/topic-display' element={<TopicDisplay />} />
          <Route path="test" element={<Test />} />
          
        </Routes>
      </BrowserRouter>

      <Footer />
    </>
  );
}

export default App;
