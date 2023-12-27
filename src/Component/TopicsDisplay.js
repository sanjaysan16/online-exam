import React, { useEffect, useState } from "react";
import AddTopic from "./AddTopic";
import { Link } from "react-router-dom";
import Swal from "sweetalert2";

const TopicsDisplay = () => {
  // const[topicId,setTopicId]=useState([])
  const [topicdata, setTopicdata] = useState("");
  const [topicList, setTopicList] = useState();
  const [percentage, setPercentage] = useState();
  const [passPercentage, setPassPercentage] = useState();
  const [name, setName] = useState();

  var url = window.location.href.includes("?");
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const examIds = urlParams.get("examId");
  const examNames = urlParams.get("examName");
  console.log(examIds);
  useEffect(() => {
    if (url) {
      displayTopic();
    }
  }, []);

  const displayTopic = () => {
    fetch(
      `https://localhost:8443/onlineexamapplication/control/ExamTopicMapping?examId=${examIds}`,
      { credentials: "include" }
    )
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setTopicdata(data);

        data.topicList.map((id) => {
          const topicId = id.topicId;
          return setTopicList(topicId);
        });
      });
  };

  const editTopic = (topicId) => {
    setTopicList(topicId);

    fetch(
      `https://localhost:8443/onlineexamapplication/control/GetTopicMaster?examId=${examIds}&&topicId=${topicId}`,
      {
        credentials: "include",
      }
    )
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        const responsePercentage = data.TopicDetails.percentage;
        const responseTopicPassPercentage =
          data.TopicDetails.topicPassPercentage;
        const responseTopicName = data.TopicName.topicName;
        setPercentage(responsePercentage);
        setPassPercentage(responseTopicPassPercentage);
        setName(responseTopicName);
       
      });
      
  };

  const deleteTopic = (topicId) => {
    //console.log(topicId)
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!",
    }).then((result) => {
      if (result.isConfirmed) {
        fetch(
          `https://localhost:8443/onlineexamapplication/control/DeleteTopicMaster?examId=${examIds}&&topicId=${topicId}`,
          { credentials: "include" }
        ).then((response) => response.json());
        Swal.fire({
          title: "Deleted!",
          text: "Your file has been deleted.",
          icon: "success",
        }).then((data) => {
          console.log(data);
          window.location.reload();
        });
      }
    });
  };
  function addTopic() {
    setName("");
    setPassPercentage("");
    setPercentage("");
    setTopicList("");
  }

  return (
    <>
      <AddTopic
        topics={topicList}
        percentage={percentage}
        passPercentage={passPercentage}
        name={name}
        examId={examIds}
      />
     
      <div>
        <h3>
          {examNames}
          <button
            className="btn btn-success m-1"
            data-bs-toggle="modal"
            data-bs-target="#addTopic"
            style={{ float: "right" }}
            onClick={() => addTopic()}
          >
            AddTopic
          </button>
        </h3>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th scope="col">TopicName</th>
            <th scope="col"></th>
            <th scope="col"></th>
          </tr>
        </thead>

        <tbody>
          {topicdata &&
            topicdata.topicList.map((data) => {
              return (
                <tr key={data.topicId}>
                  <th scope="row">{data.topicName}</th>
                  <th scope="row"></th>
                  <th scope="row">
                    <button
                      className="btn btn-danger m-1"
                      title="delete topic"
                      style={{ float: "right" }}
                      onClick={() => deleteTopic(data.topicId)}
                    >
                      Delete
                    </button>
                    <button
                      className="btn btn-info m-1"
                      title="edit topic"
                      style={{ float: "right" }}
                      data-bs-toggle="modal"
                      data-bs-target="#addTopic"
                      onClick={() => {
                        editTopic(data.topicId);
                      }}
                    >
                      Edit
                    </button>
                    <a href={`add-question?topicId=${data.topicId}`}>
                      {" "}
                      <button
                        className="btn btn-success m-1"
                        style={{ float: "right" }}
                      >
                        Add Questions
                      </button>
                    </a>
                  </th>
                </tr>
              );
            })}
        </tbody>
      </table>
    </>
  );
};

export default TopicsDisplay;

// import React, { useEffect, useState } from 'react'
// import AddTopic from '../AddTopic';

// const TopicsDisplay = (topicName) => {
//     const[topicss,setTopics]=useState([])
//     const[topicNames,setTopicNames]=useState("")
//     const[topicId,setTopicId]=useState([])
//     console.log(topicName);
//     useEffect(()=>{
//         topicCall()
//     },[]);

//     function topicCall(){
//         fetch("https://localhost:8443/onlineexamapplication/control/ExamTopicMapping")
//         .then((response)=>response.json())
//         .then((data)=> {
//             var s=data.topicsName;
//             var d=data.topicId;
//             setTopics(s)

//         })

//     }
//     function deleteTopic(){
//         console.log();
//         fetch(`https://localhost:8443/onlineexamapplication/control/DeleteTopicMaster?examId=${123}`)

//     }

//     function viewTopic(topic){
//         console.log(topic);
//         fetch(`https://localhost:8443/onlineexamapplication/control/EditTopicMaster?examId=${123}&topicId=${topic}`)
//         .then((response)=>response.json())
//         .then((data)=> {
//             const topicName=(data.topicName);
//             setTopicNames(topicName)
//             setTopicId(data.topics)
//             console.log(topicName)
//         })
//     }

//     return (
//         <>
//         <AddTopic topicName={topicNames}/>
//             <nav className="arrow" aria-label="breadcrumb">
//                 <ol class="breadcrumb">
//                     <li class="breadcrumb-item  "><a className="text-decoration-none"href="/">Home</a></li>
//                     <li class="breadcrumb-item " ><a className="text-decoration-none"href='add-exam'>Add Exam</a></li>
//                     <li class="breadcrumb-item active " >Topics Display</li>
//                 </ol>
//             </nav>

//             <table className='table'>
//                 <thead>
//                     <tr>
//                         <th scope='col'>TopicId</th>
//                         <th scope='col'>TopicName</th>
//                         <th scope='col'><button className='btn btn-success m-1' data-bs-toggle="modal" data-bs-target="#addTopic" style={{ "float": "right" }}>AddTopic</button></th>
//                     </tr>
//                 </thead>
//                 {topicss && topicss.map((t)=>{

//                     // console.log(t)
//                     return(<>

//                     <tbody>
//                     <tr>

//                         <th scope='row'>{t.topicId}</th>
//                         <th scope='row'>{t}</th>
//                         <th scope='row'>
//                         <button className='btn btn-danger m-1' title="delete topic" style={{ "float": "right" }} onClick={()=>deleteTopic(t.topicId)}>Delete</button>
//                         <button className='btn btn-info m-1' title="edit topic" style={{ "float": "right" }} data-bs-toggle="modal" data-bs-target="#addTopic" onClick={()=>viewTopic()}>Edit</button>
//                         <a href='add-question'> <button className='btn btn-success m-1' style={{ "float": "right" }}>Add Questions</button></a>
//                         </th>
//                     </tr>
//                 </tbody>
//                 </>)
//                 })   }

//             </table>

//         </>
//     )
// }

// export default TopicsDisplay
