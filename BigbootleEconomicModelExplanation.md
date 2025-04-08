# Bigbootle Economic Model Explanation

This document outlines the economic model for the $B3TR token within the Bigbootle application, covering the team share allocation mechanism and the rules for user token acquisition and distribution.

## 1. Team Share

The $B3TR$ token share allocated to the team consists of two parts:

1.  **Base Allocation:** 100% of this portion is allocated to the team.
2.  **Votes Allocation:** The proportion taken from this allocation is dynamically linked to the market price ($P$) of the $B3TR token. The specific calculation formula is as follows:

Let $P$ be the price of $B3TR$, and $R$ be the proportion the team takes from the Votes Allocation pool:

```math
R =
\begin{cases}
0.3, & \text{if } P \leq 0.5 \\
0.3 \times \frac{1 - P}{0.5}, & \text{if } 0.5 < P < 1 \\
0, & \text{if } P \geq 1
\end{cases}
```

**Parameter Descriptions:**

* $P$: The real-time price of the $B3TR token.
* $R$: The proportion of tokens the team can withdraw from the current Votes Allocation pool (as a percentage of that pool's total).

**Mechanism Interpretation:**

* When the price of $B3TR$ is less than or equal to $0.5 USD, the team consistently receives 30% of the Votes Allocation pool.
* When the price is between $0.5 USD and $1 USD, the proportion the team receives decreases linearly from 30% as the price increases. For example, if the price is $0.75 USD, the proportion $R = 0.3 \times (1 - 0.75) / 0.5 = 0.15$, meaning 15%.
* When the price reaches or exceeds $1 USD, the team no longer takes a share from the Votes Allocation pool (proportion is 0%).
    * *This design aims to inversely correlate team earnings (within a specific range) with token value, encouraging the team to focus on increasing the long-term value of $B3TR$.*

## 2. User Token Distribution

Users earn points by participating in eco-friendly actions (purchasing and uploading receipts for large-volume beverages) and share a weekly $B3TR$ token reward pool based on their accumulated points.

### 1. Point Acquisition Rules

Users earn points ($P$) based on the volume $V$ (in ml) of the single beverage bottle purchased:

```math
P =
\begin{cases}
1 \text{ Point}, & \text{if } V < 700 \text{ ml} \\
10 \text{ Points}, & \text{if } 700 \text{ ml} \leq V < 2000 \text{ ml} \\
15 \text{ Points}, & \text{if } V \geq 2000 \text{ ml}
\end{cases}
```

**Rule Description:**

* **Small Beverage** (Volume < 700ml): Earn **1** Point
* **Medium Beverage** (Volume ≥ 700ml and < 2000ml): Earn **10** Points
* **Large Beverage** (Volume ≥ 2000ml): Earn **15** Points

**Example Calculations:**

* Purchase a **500ml** beverage: Volume < 700ml, earn **1** Point.
* Purchase a **1500ml** beverage: Volume between 700ml and 2000ml, earn **10** Points.
* Purchase a **2500ml** beverage: Volume ≥ 2000ml, earn **15** Points.

### 2. Weekly $B3TR Reward Distribution

The amount of $B3TR$ tokens a user can claim each week depends on the proportion of their weekly points relative to the total points of all users for that week, subject to a maximum cap.

**Calculation Formula:**

```math
\text{User Weekly } B3TR \text{ Reward} = \min\left( \frac{\text{Weekly Total } B3TR \text{ Pool} \times \text{User Weekly Points}}{\text{Total Weekly Points (All Users)}}, 300 \right)
```

**Rule Description:**

* Each week, a total $B3TR$ reward pool is allocated for distribution among all participating users.
* A user's share of the reward is proportional to the points they contributed during that week.
* **Claim Cap:** Each user can claim a maximum of **300 $B3TR** per week.
* **Rollover Mechanism:** If the $B3TR$ in the weekly reward pool is not fully distributed (e.g., because users hit their claim caps, or total points earned are low), the remaining undistributed $B3TR$ will automatically roll over and be added to the reward pool for the following week.

---

*Disclaimer: This economic model may be subject to adjustments based on project development and community governance. Please refer to the latest official information.*
