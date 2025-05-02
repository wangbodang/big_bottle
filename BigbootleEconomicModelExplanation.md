
# Bigbootle Economic Model Explanation

This document outlines the economic model for the $B3TR token within the Bigbootle application, covering the team share allocation mechanism and the rules for user token acquisition and distribution.

## 1. Team Share

The $B3TR$ token share allocated to the team consists of two parts:

1.  **Base Allocation:** 100% of this portion is allocated to the team.
2.  **Votes Allocation:** The team receives a fixed **40%** share from this allocation pool in each distribution period.

**Mechanism Interpretation:**

* A defined portion of tokens is designated as the "Votes Allocation" pool periodically.
* From this specific pool, the team consistently receives **40%** of the tokens allocated during that period. The remaining **60%** may be allocated for other purposes as defined by the project's governance or tokenomics (such as user rewards, ecosystem development, etc.).

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
* **Claim Cap:** Each user can claim a maximum of **50 $B3TR** per week.
* **Rollover Mechanism:** If the $B3TR$ in the weekly reward pool is not fully distributed (e.g., because users hit their claim caps, or total points earned are low), the remaining undistributed $B3TR$ will automatically roll over and be added to the reward pool for the following week.

---

*Disclaimer: This economic model may be subject to adjustments based on project development and community governance. Please refer to the latest official information.*
