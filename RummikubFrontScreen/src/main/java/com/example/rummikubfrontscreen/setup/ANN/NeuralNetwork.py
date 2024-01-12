import numpy as np
import math
import torch
import torch.nn as nn
import torch.nn.functional as F

#import Board
#import GameSetup

#WILL HAVE TO IMPORT Board.java as well as other necessary components of the game
#Most vars here are placeholders until a proper connection between java and pythong can be made


##IN PROGRESS
##NOT REMOTELY FINISHED

##Needs code to connect it to the MCTS, further optimization with the current Rummikub game code structure is needed

#Uses a Resnet to predict moves as well as assesing the current value of the board
class RummikubNet(nn.Module):
    def __init__(self, BOARD/GAMESETUP, num_resBlocks, num_hidden): #num_hidden specifies the amount of hidden layers you would want in the NN
        super().__init__()

        #Process input tiles
        self.startBlock = nn.Sequential(
            #Convolutional layer to capture features of different types of tiles
            nn.Conv2d(GameSetup.getAllTiles().unique(), num_hidden, kernel_size=3, padding=1),
            #batch normalization for stabilization during training
            nn.BatchNorm2d(num_hidden),
            #ReLU activation for introducing non-linearity
            nn.ReLU()
        )
        
        # Backbone consisting of multiple residual blocks
        self.backBone = nn.ModuleList(
            [ResBlock(num_hidden) for _ in range(num_resBlocks)]
        )
        
        #Policy head for move prediction
        self.policyHead = nn.Sequential(
            #convolutional layer to extract features for move prediction
            nn.Conv2d(num_hidden, 32, kernel_size=3, padding=1),
            nn.BatchNorm2d(32),
            nn.ReLU(),

            #flatten to convert 3D tensor to 1D for fully connected layers
            nn.Flatten(),
            #fully connected layer for predicting moves
            nn.Linear(32 * Board.GETXLENGTH * Board.GETYLENGTH, Board.GETMOVES) #last parameter likely has to be modified, has to get input the amount of possible moves that can be done
        )
        
        #Value head for assessing board position
        self.valueHead = nn.Sequential(
            # Convolutional layer for feature extraction
            nn.Conv2d(num_hidden, 3, kernel_size=3, padding=1),
            nn.BatchNorm2d(3),
            nn.ReLU(),
            nn.Flatten(),

            # Fully connected layer for predicting the value of the current position
            nn.Linear(3 *  Board.GETXLENGTH * Board.GETYLENGTH, 1),
            #Tanh activation to squash the output to the range [-1, 1]
            nn.Tanh() #(Gives the "rating" of the game state, hence why it's between -1 & 1 (improvements or worsening))
        )
        
    def forward(self, x):
        # Forward pass through the network

        #Process input tiles
        x = self.startBlock(x)
        
        #apply each residual block in the backbone
        for resBlock in self.backBone:
            x = resBlock(x)
        
        #Policy network prediction
        policy = self.policyHead(x)
        
        # Value network prediction
        value = self.valueHead(x)
        
        #returns values
        return policy, value

# Residual block class
class ResBlock(nn.Module):
    def __init__(self, num_hidden):
        super().__init__()

        #two convolutional layers with batch normalization
        self.conv1 = nn.Conv2d(num_hidden, num_hidden, kernel_size=3, padding=1)
        self.bn1 = nn.BatchNorm2d(num_hidden)
        self.conv2 = nn.Conv2d(num_hidden, num_hidden, kernel_size=3, padding=1)
        self.bn2 = nn.BatchNorm2d(num_hidden)
        
    def forward(self, x):
        #store the input as the residual
        residual = x
        
        #first convolutional layer followed by batch normalization and ReLU activation
        x = F.relu(self.bn1(self.conv1(x)))
        
        #second convolutional layer followed by batch normalization
        x = self.bn2(self.conv2(x))
        
        #add the residual to the output
        x += residual
        
        #apply ReLU activation to the final output
        x = F.relu(x)
        
        #return the final output of the residual block
        return x