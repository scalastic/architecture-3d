# Architecture 3D

## Starts all components

* Starts the MongoDB database:
```
mkdir -p ./data
docker pull mongo:latest
docker run --name local-mongodb -d -p 27017:27017 -v ~/data:/data/db mongo
```

* Starts the Frontend+API listening to port 80:
```
sbt "run 80"
```



## Exemple de JSON généré par l'application


```json
{
  "1f8kme115-93hh16hdf": {
    "type": "component",
    "data": {
      "componentId": "ai.aws-amazon-pinpoint",
      "position": {
        "x": 3,
        "y": -3.5
      },
      "rotation": 0,
      "opacity": 1
    }
  },
  "1f8kmjfvn-1cm186555": {
    "type": "component",
    "data": {
      "componentId": "security.aws-amazon-cloud-directory",
      "position": {
        "x": -1,
        "y": -5
      },
      "rotation": 1.5707963267948966,
      "opacity": 1,
      "layerId": "default"
    }
  },
  "1f8kmmugp-18sbjjfk5u": {
    "type": "component",
    "data": {
      "componentId": "monitoring.kibana",
      "position": {
        "x": -1,
        "y": -2
      },
      "rotation": 0,
      "opacity": 1
    }
  },
  "1f8kmppv5-7hl9d58nu": {
    "type": "line-group",
    "data": {
      "strokeStyle": "#e61898",
      "lineWidth": 0.05,
      "lineDash": 3,
      "arrowAnchorIndices": {
        "0": true,
        "3": true
      },
      "anchors": [
        {
          "type": 1,
          "index": 7,
          "id": "1f8kmmugp-18sbjjfk5u",
          "x": 0,
          "y": -1.5
        },
        {
          "type": 0,
          "x": 1,
          "y": -1.5
        },
        {
          "type": 0,
          "x": 1,
          "y": -4.5
        },
        {
          "type": 1,
          "index": 7,
          "id": "1f8kmjfvn-1cm186555",
          "x": 0,
          "y": -5
        }
      ],
      "lines": [
        [
          0,
          1
        ],
        [
          1,
          2
        ],
        [
          2,
          3
        ]
      ],
      "layerId": "default"
    }
  },
  "1f8kmqcvd-1t6e2t30h4": {
    "type": "line-group",
    "data": {
      "strokeStyle": "#e61898",
      "lineWidth": 0.05,
      "lineDash": 3,
      "arrowAnchorIndices": {},
      "anchors": [
        {
          "type": 0,
          "x": 1,
          "y": -3
        },
        {
          "type": 1,
          "index": 6,
          "id": "1f8kme115-93hh16hdf",
          "x": 3,
          "y": -3.5
        }
      ],
      "lines": [
        [
          0,
          1
        ]
      ],
      "layerId": "default"
    }
  },
  "1f8knbjfu-njbhojjql": {
    "type": "component",
    "data": {
      "componentId": "generic.pc",
      "position": {
        "x": 6.5,
        "y": -3.5
      },
      "rotation": 1.5707963267948966,
      "opacity": 1,
      "primaryColor": "#999999",
      "backgroundColor": "#FFFFFF",
      "imagePath": "/assets/img-sass/blank.png",
      "iconColor": "#333333"
    }
  },
  "1f8knbrk9-1d65kucoc3": {
    "type": "line-group",
    "data": {
      "strokeStyle": "#53E618",
      "lineWidth": 0.05,
      "lineDash": 1,
      "arrowAnchorIndices": {},
      "anchors": [
        {
          "type": 1,
          "index": 6,
          "id": "1f8knbjfu-njbhojjql",
          "x": 6.5,
          "y": -3
        },
        {
          "type": 1,
          "index": 7,
          "id": "1f8kme115-93hh16hdf",
          "x": 4,
          "y": -3
        }
      ],
      "lines": [
        [
          0,
          1
        ]
      ]
    }
  },
  "1f8kn87s7-1jh7v1e70j": {
    "type": "area",
    "data": {
      "anchors": [
        {
          "x": -2,
          "y": -6
        },
        {
          "x": 5,
          "y": -6
        },
        {
          "x": 5,
          "y": 0
        },
        {
          "x": -2,
          "y": 0,
          "isPoint": true
        },
        {
          "x": -2,
          "y": -1
        }
      ],
      "lineColor": "#CCCCCC",
      "lineWidth": 0.05,
      "fillColor": "#DFDFDF",
      "shadowLevel": 3,
      "zIndex": 0,
      "areaType": 1,
      "wallHeight": 4,
      "layerId": "default",
      "wallColor": "#606060"
    }
  },
  "1f8knsf7v-2n79jnltce": {
    "type": "component",
    "data": {
      "componentId": "computation.aws-ec2",
      "position": {
        "x": -1,
        "y": -9
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8kntqqp-17rqjfq237": {
    "type": "component",
    "data": {
      "componentId": "computation.aws-lambda",
      "position": {
        "x": -6,
        "y": -4
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8knttdj-6m3aepjii": {
    "type": "component",
    "data": {
      "componentId": "computation.aws-lambda",
      "position": {
        "x": -6,
        "y": -5
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8knu0u3-2drer8u6kb": {
    "type": "component",
    "data": {
      "componentId": "computation.aws-lambda",
      "position": {
        "x": -6,
        "y": -6
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8knu2rh-hq93lapp9": {
    "type": "component",
    "data": {
      "componentId": "computation.aws-lambda",
      "position": {
        "x": -6,
        "y": -7
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8knu5s6-fafb9q3n9": {
    "type": "component",
    "data": {
      "componentId": "computation.aws-lambda",
      "position": {
        "x": -6,
        "y": -3
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8knuhql-29hv8de026": {
    "type": "area",
    "data": {
      "anchors": [
        {
          "x": -6.5,
          "y": -7.5
        },
        {
          "x": -4.5,
          "y": -7.5
        },
        {
          "x": -4.5,
          "y": -1.5
        },
        {
          "x": -6.5,
          "y": -1.5
        }
      ],
      "lineColor": "#CCCCCC",
      "lineWidth": 0.05,
      "fillColor": "#E6CD18",
      "shadowLevel": 1,
      "zIndex": 0
    }
  },
  "1f8ko2h13-1e5beqm2bq": {
    "type": "component",
    "data": {
      "componentId": "computation.aws-amazon-elastic-container-registry",
      "position": {
        "x": -9,
        "y": -4
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8ko2qiu-them76m37": {
    "type": "component",
    "data": {
      "componentId": "computation.aws-fargate",
      "position": {
        "x": -9,
        "y": -6
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8ko31fu-1mprnnq6b5": {
    "type": "component",
    "data": {
      "componentId": "computation.aws-batch",
      "position": {
        "x": -5,
        "y": 0
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8ko39m5-1k1ncl4n46": {
    "type": "component",
    "data": {
      "componentId": "computation.aws-amazon-elastic-container-service",
      "position": {
        "x": -9,
        "y": -2
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8ko3ei8-1tcp2pgeha": {
    "type": "component",
    "data": {
      "componentId": "computation.azure-container-instances",
      "position": {
        "x": -4,
        "y": -9
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  },
  "1f8ko3jb8-1d5vj1cgr7": {
    "type": "component",
    "data": {
      "componentId": "computation.azure-container-instances",
      "position": {
        "x": -4,
        "y": -9
      },
      "rotation": 1.5707963267948966,
      "opacity": 0.5
    }
  }
}
```