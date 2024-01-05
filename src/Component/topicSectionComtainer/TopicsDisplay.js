
import React, { useEffect, useState } from "react";
import AddTopic from "./AddTopic";
import { Link } from "react-router-dom";
import Swal from "sweetalert2";
import useStateRef from "react-usestateref";
import { port, protocol } from "../fetchConst";
import WarningModal from "../adminSectionContainer/WarningModal";

const TopicsDisplay = () => {

  const [topicdata, setTopicdata, refTopicData] = useStateRef("");
  const [topicList, setTopicList] = useState();
  const [percentage, setPercentage] = useState();
  const [passPercentage, setPassPercentage] = useState();
  const [name, setName] = useState();
  const[isAdmin,setIsAdmin]=useState(false)
  const[message,setMessage]=useState('')

  const uri = `${protocol}://${window.location.hostname}:${port}`;

  var url = window.location.href.includes("?");
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const examIds = urlParams.get("examId");
  const examNames = urlParams.get("examName");
  console.log(urlParams);
  console.log(examIds);
  console.log(examNames);
  useEffect(() => {
    if (url) {
      displayTopic();
    }
    adminOrUserCheck();
  }, []);
  const adminOrUserCheck = () => {
    fetch(`${uri}/onlineexamapplication/control/check-admin-or-user`, {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.errMsg == "notAdmin") {
          setIsAdmin(true);
          setMessage(data.message);
          document.getElementById("topicDisplayAdminCheck").click();
        }
      })
      .catch((error) => console.error("Error:", error));
  };

  const displayTopic = () => {
    fetch(
      `${uri}/onlineexamapplication/control/TopicsList?examId=${examIds}`,
      { credentials: "include" }
    )
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setTopicdata(data.topicList);

        data.topicList.map((id) => {
          const topicId = id.topicId;
          return setTopicList(topicId);
        });
      })
      .catch(error => console.error('Error:', error));
  };

  const editTopic = (topicId) => {
    setTopicList(topicId);

    fetch(
      `${uri}/onlineexamapplication/control/GetTopicMaster?examId=${examIds}&&topicId=${topicId}`,
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
        console.log(responseTopicPassPercentage)
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
          `${uri}/onlineexamapplication/control/DeleteTopicMaster?examId=${examIds}&&topicId=${topicId}`,
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

     <p
        id="examdashbordcheck"
        data-bs-toggle="modal"
        data-bs-target="#exampleModal"
      ></p>
      <WarningModal message={message} />

      {isAdmin ? (<></>):(
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
            className="btn btn-outline-success m-1"
            data-bs-toggle="modal"
            data-bs-target="#addTopic"
            style={{ float: "right" }}
            onClick={() => addTopic()}
          >
            AddTopic
          </button>
        </h3>
      </div>



      <table className="table border">
        <thead>
          <tr>
            <th scope="col">TopicName</th>
            <th scope="col"></th>
            <th scope="col"></th>
          </tr>
        </thead>

        <tbody>

          {refTopicData.current.length != 0 ?
            refTopicData.current && refTopicData.current.map((data) => {
              return (
                <tr key={data.topicId}>
                  <th scope="row">{data.topicName}</th>
                  <th scope="row"></th>
                  <th scope="row">
                    <button
                      className="btn btn-outline-danger m-1"
                      title="delete topic"
                      style={{ float: "right" }}
                      onClick={() => deleteTopic(data.topicId)}
                    >
                      Delete
                    </button>
                    <button
                      className="btn btn-outline-info m-1"
                      
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
                        className="btn btn-outline-success m-1"
                        style={{ float: "right" }}
                      >
                        Add Questions
                      </button>
                    </a>
                  </th>
                </tr>
              );
            }) : <h1>No topics Available</h1>}
        </tbody>
      </table>
      </>
     )}
    </>
  );
};

export default TopicsDisplay;